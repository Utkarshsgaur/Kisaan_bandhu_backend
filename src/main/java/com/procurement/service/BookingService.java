package com.procurement.service;

import com.procurement.dto.BookingRequest;
import com.procurement.entity.*;
import com.procurement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService {

 private final UserRepository users;
 private final FarmerRepository farmers;
 private final ScheduleRepository schedules;
 private final BookingRepository bookings;
 private final WaitingListRepository waiting;
 private final NotificationService notifications;

 public BookingService(
         UserRepository users,
         FarmerRepository farmers,
         ScheduleRepository schedules,
         BookingRepository bookings,
         WaitingListRepository waiting,
         NotificationService notifications
 ) {
  this.users = users;
  this.farmers = farmers;
  this.schedules = schedules;
  this.bookings = bookings;
  this.waiting = waiting;
  this.notifications = notifications;
 }

 private Farmer farmer(String phone) {
  User u = users.findByPhoneNumber(phone)
          .orElseThrow(() -> new NoSuchElementException("User not found"));

  return farmers.findByUserId(u.getId())
          .orElseThrow(() -> new NoSuchElementException("Farmer profile not found"));
 }

 @Transactional
 public Object create(String phone, BookingRequest r) {

  Farmer f = farmer(phone);

  ProcurementSchedule s = schedules.findByIdForUpdate(r.scheduleId())
          .orElseThrow(() -> new NoSuchElementException("Schedule not found"));

  if (s.getStatus() != ScheduleStatus.OPEN) {
   throw new IllegalArgumentException("Schedule is not open");
  }

  if (bookings.existsByFarmerIdAndScheduleIdAndStatusIn(
          f.getId(),
          s.getId(),
          List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
  )) {
   throw new IllegalArgumentException(
           "You already have a booking for this schedule"
   );
  }

  BigDecimal available = s.getMaxCapacityQuintal()
          .subtract(s.getBookedCapacityQuintal());

  if (available.compareTo(r.quantityQuintal()) >= 0) {

   Booking b = new Booking();

   b.setFarmer(f);
   b.setSchedule(s);
   b.setQuantityQuintal(r.quantityQuintal());
   b.setStatus(BookingStatus.CONFIRMED);

   Booking saved = bookings.saveAndFlush(b);

   notifications.create(
           f,
           saved,
           NotificationType.BOOKING_CONFIRMED,
           "Your procurement booking is confirmed"
   );

   return saved;
  }

  WaitingListEntry w = new WaitingListEntry();

  w.setFarmer(f);
  w.setSchedule(s);
  w.setQuantityQuintal(r.quantityQuintal());

  w.setQueuePosition(
          waiting.countByScheduleIdAndStatus(
                  s.getId(),
                  WaitingStatus.WAITING
          ) + 1
  );

  w.setStatus(WaitingStatus.WAITING);

  return waiting.save(w);
 }

 @Transactional
 public Booking cancel(String phone, Long id) {

  Farmer f = farmer(phone);

  Booking b = bookings.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Booking not found"));

  if (!b.getFarmer().getId().equals(f.getId())) {
   throw new IllegalArgumentException("Not your booking");
  }

  if (!(b.getStatus() == BookingStatus.PENDING
          || b.getStatus() == BookingStatus.CONFIRMED)) {

   throw new IllegalArgumentException(
           "Booking cannot be cancelled now"
   );
  }

  b.setStatus(BookingStatus.CANCELLED);

  Booking saved = bookings.saveAndFlush(b);

  notifications.create(
          f,
          saved,
          NotificationType.BOOKING_CANCELLED,
          "Your booking has been cancelled"
  );

  return saved;
 }

 public List<Booking> mine(String phone) {
  return bookings.findByFarmerIdOrderByBookedAtDesc(
          farmer(phone).getId()
  );
 }

 @Transactional(readOnly = true)
 public Booking getById(String phone, Long id) {

  Farmer f = farmer(phone);

  Booking b = bookings.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Booking not found"));

  if (!b.getFarmer().getId().equals(f.getId())) {
   throw new IllegalArgumentException("Not your booking");
  }

  return b;
 }

 public List<Booking> scheduleBookings(Long scheduleId) {
  return bookings.findByScheduleIdOrderByBookedAtAsc(scheduleId);
 }
}