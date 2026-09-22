package com.procurement.service;

import com.procurement.entity.Booking;
import com.procurement.entity.BookingStatus;
import com.procurement.entity.Farmer;
import com.procurement.entity.User;
import com.procurement.entity.WaitingListEntry;
import com.procurement.entity.WaitingStatus;
import com.procurement.repository.BookingRepository;
import com.procurement.repository.FarmerRepository;
import com.procurement.repository.ScheduleRepository;
import com.procurement.repository.UserRepository;
import com.procurement.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueService {

 private final BookingRepository bookings;
 private final ScheduleRepository schedules;
 private final FarmerRepository farmers;
 private final UserRepository users;
 private final WaitingListRepository waiting;

 private final int avgMinutes;

 public QueueService(
         BookingRepository bookings,
         ScheduleRepository schedules,
         FarmerRepository farmers,
         UserRepository users,
         WaitingListRepository waiting,
         @Value("${app.queue.avg-processing-minutes:5}") int avgMinutes
 ) {
  this.bookings = bookings;
  this.schedules = schedules;
  this.farmers = farmers;
  this.users = users;
  this.waiting = waiting;
  this.avgMinutes = avgMinutes;
 }

 public Map<String, Object> mine(String phone, Long bookingId) {

  User user = users.findByPhoneNumber(phone)
          .orElseThrow(() -> new RuntimeException("User not found"));

  Farmer farmer = farmers.findByUserId(user.getId())
          .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

  Booking target = bookings.findById(bookingId)
          .orElseThrow(() -> new RuntimeException("Booking not found"));

  // Security check:
  // Farmer sirf apni booking ki queue detail dekh sakta hai.
  if (!target.getFarmer().getId().equals(farmer.getId())) {
   throw new IllegalArgumentException("Not your booking");
  }

  List<Booking> list = bookings
          .findByScheduleIdOrderByBookedAtAsc(
                  target.getSchedule().getId()
          )
          .stream()
          .filter(b ->
                  b.getStatus() == BookingStatus.PENDING
                          || b.getStatus() == BookingStatus.CONFIRMED
          )
          .toList();

  int position = 1;

  for (int i = 0; i < list.size(); i++) {

   if (list.get(i).getId().equals(bookingId)) {
    position = i + 1;
    break;
   }
  }

  Map<String, Object> response = new LinkedHashMap<>();

  response.put("bookingId", bookingId);
  response.put("tokenNumber", "#" + bookingId);
  response.put("currentPosition", position);
  response.put("totalInQueue", list.size());
  response.put(
          "estimatedWaitingMinutes",
          Math.max(0, (position - 1) * avgMinutes)
  );
  response.put("status", target.getStatus().name());

  return response;
 }

 public List<WaitingListEntry> waiting(Long scheduleId) {

  return waiting.findByScheduleIdAndStatusOrderByQueuePositionAsc(
          scheduleId,
          WaitingStatus.WAITING
  );
 }
}