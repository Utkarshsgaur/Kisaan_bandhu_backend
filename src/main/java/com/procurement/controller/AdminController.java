package com.procurement.controller;

import com.procurement.dto.AdminDtos.*;
import com.procurement.entity.*;
import com.procurement.repository.*;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

 private final CropRepository crops;
 private final CentreRepository centres;
 private final SeasonRepository seasons;
 private final ScheduleRepository schedules;
 private final UserRepository users;
 private final FarmerRepository farmers;
 private final BookingRepository bookings;
 private final WaitingListRepository waiting;
 private final ProcurementRecordRepository procurement;
 private final NotificationRepository notifications;
 private final SeasonCropRepository seasonCrops;

 public AdminController(
         CropRepository crops,
         CentreRepository centres,
         SeasonRepository seasons,
         ScheduleRepository schedules,
         UserRepository users,
         FarmerRepository farmers,
         BookingRepository bookings,
         WaitingListRepository waiting,
         ProcurementRecordRepository procurement,
         NotificationRepository notifications,
         SeasonCropRepository seasonCrops
 ) {
  this.crops = crops;
  this.centres = centres;
  this.seasons = seasons;
  this.schedules = schedules;
  this.users = users;
  this.farmers = farmers;
  this.bookings = bookings;
  this.waiting = waiting;
  this.procurement = procurement;
  this.notifications = notifications;
  this.seasonCrops = seasonCrops;
 }

 // =========================================================
 // EXISTING PHOTO APIs
 // DO NOT REMOVE
 // =========================================================

 @GetMapping("/dashboard")
 public Map<String, Object> dashboard() {

  return Map.of(
          "totalFarmers", farmers.count(),
          "totalBookings", bookings.count(),
          "totalProcurementRecords", procurement.count()
  );
 }

 @GetMapping("/farmers")
 public List<Farmer> farmerList() {
  return farmers.findAll();
 }

 @GetMapping("/bookings")
 public List<Booking> bookingList() {
  return bookings.findAll();
 }

 @GetMapping("/queue")
 public List<WaitingListEntry> queue(
         @RequestParam Long scheduleId
 ) {
  return waiting.findByScheduleIdAndStatusOrderByQueuePositionAsc(
          scheduleId,
          WaitingStatus.WAITING
  );
 }

 @PostMapping("/crops")
 public Crop addCrop(
         @Valid @RequestBody CropRequest r
 ) {

  Crop c = new Crop();

  c.setCropName(r.cropName());
  c.setCropCode(r.cropCode());

  if (r.active() != null) {
   c.setActive(r.active());
  }

  return crops.save(c);
 }

 @PutMapping("/crops/{id}")
 public Crop updateCrop(
         @PathVariable Long id,
         @Valid @RequestBody CropRequest r
 ) {

  Crop c = crops.findById(id)
          .orElseThrow(() ->
                  new NoSuchElementException("Crop not found")
          );

  c.setCropName(r.cropName());
  c.setCropCode(r.cropCode());

  if (r.active() != null) {
   c.setActive(r.active());
  }

  return crops.save(c);
 }

 @DeleteMapping("/crops/{id}")
 public void deleteCrop(
         @PathVariable Long id
 ) {

  Crop c = crops.findById(id)
          .orElseThrow(() ->
                  new NoSuchElementException("Crop not found")
          );

  c.setActive(false);

  crops.save(c);
 }

 @PostMapping("/centres")
 public ProcurementCentre addCentre(
         @Valid @RequestBody CentreRequest r
 ) {

  ProcurementCentre c = new ProcurementCentre();

  applyCentre(c, r);

  return centres.save(c);
 }

 @PutMapping("/centres/{id}")
 public ProcurementCentre updateCentre(
         @PathVariable Long id,
         @Valid @RequestBody CentreRequest r
 ) {

  ProcurementCentre c = centres.findById(id)
          .orElseThrow(() ->
                  new NoSuchElementException("Centre not found")
          );

  applyCentre(c, r);

  return centres.save(c);
 }

 @DeleteMapping("/centres/{id}")
 public void deleteCentre(
         @PathVariable Long id
 ) {

  ProcurementCentre c = centres.findById(id)
          .orElseThrow(() ->
                  new NoSuchElementException("Centre not found")
          );

  c.setActive(false);

  centres.save(c);
 }

 private void applyCentre(
         ProcurementCentre c,
         CentreRequest r
 ) {

  c.setCentreCode(r.centreCode());
  c.setCentreName(r.centreName());
  c.setDistrict(r.district());
  c.setState(r.state());
  c.setAddress(r.address());
  c.setPincode(r.pincode());
  c.setContactPhone(r.contactPhone());

  if (r.active() != null) {
   c.setActive(r.active());
  }
 }

 @PostMapping("/schedules")
 public ProcurementSchedule addSchedule(
         @Valid @RequestBody ScheduleRequest r
 ) {

  return saveSchedule(
          new ProcurementSchedule(),
          r
  );
 }

 @PutMapping("/schedules/{id}")
 public ProcurementSchedule updateSchedule(
         @PathVariable Long id,
         @Valid @RequestBody ScheduleRequest r
 ) {

  ProcurementSchedule schedule =
          schedules.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Schedule not found"
                          )
                  );

  return saveSchedule(
          schedule,
          r
  );
 }

 private ProcurementSchedule saveSchedule(
         ProcurementSchedule s,
         ScheduleRequest r
 ) {

  s.setCentre(
          centres.findById(r.centreId())
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Centre not found"
                          )
                  )
  );

  s.setSeason(
          seasons.findById(r.seasonId())
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Season not found"
                          )
                  )
  );

  s.setCrop(
          crops.findById(r.cropId())
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Crop not found"
                          )
                  )
  );

  s.setScheduleDate(r.scheduleDate());
  s.setStartTime(r.startTime());
  s.setEndTime(r.endTime());
  s.setMaxCapacityQuintal(
          r.maxCapacityQuintal()
  );

  if (r.status() != null) {
   s.setStatus(r.status());
  }

  return schedules.save(s);
 }

 @DeleteMapping("/schedules/{id}")
 public void deleteSchedule(
         @PathVariable Long id
 ) {

  ProcurementSchedule s =
          schedules.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Schedule not found"
                          )
                  );

  s.setStatus(
          ScheduleStatus.CANCELLED
  );

  schedules.save(s);
 }

 @PutMapping("/farmers/{id}/status")
 @Transactional
 public Farmer farmerStatus(
         @PathVariable Long id,
         @RequestParam boolean active
 ) {

  Farmer f = farmers.findById(id)
          .orElseThrow(() ->
                  new NoSuchElementException(
                          "Farmer not found"
                  )
          );

  f.setActive(active);

  f.getUser().setActive(active);

  users.save(f.getUser());

  return farmers.save(f);
 }

 // =========================================================
 // NEW FRONTEND ADMIN APIs
 // =========================================================

 // GET /api/v1/admin/users
 @GetMapping("/users")
 public List<User> userList() {
  return users.findAll();
 }

 // POST /api/v1/admin/farmers
 @PostMapping("/farmers")
 @Transactional
 public Farmer addFarmer(
         @Valid @RequestBody FarmerRequest r
 ) {

  if (users.findByPhoneNumber(r.phoneNumber()).isPresent()) {
   throw new IllegalArgumentException(
           "Phone number already registered"
   );
  }

  boolean active =
          r.active() == null || r.active();

  User user = new User();

  user.setPhoneNumber(
          r.phoneNumber()
  );

  user.setRole(
          Role.FARMER
  );

  user.setActive(active);

  users.save(user);

  Farmer farmer = new Farmer();

  farmer.setUser(user);
  farmer.setFullName(r.fullName());
  farmer.setVillage(r.village());
  farmer.setDistrict(r.district());
  farmer.setState(r.state());
  farmer.setPincode(r.pincode());
  farmer.setAlternatePhone(
          r.alternatePhone()
  );
  farmer.setActive(active);

  return farmers.save(farmer);
 }

 // PUT /api/v1/admin/farmers/{id}
 @PutMapping("/farmers/{id}")
 @Transactional
 public Farmer updateFarmer(
         @PathVariable Long id,
         @Valid @RequestBody FarmerRequest r
 ) {

  Farmer farmer =
          farmers.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Farmer not found"
                          )
                  );

  User user = farmer.getUser();

  if (!user.getPhoneNumber().equals(
          r.phoneNumber()
  )) {

   Optional<User> existing =
           users.findByPhoneNumber(
                   r.phoneNumber()
           );

   if (existing.isPresent()
           && !existing.get()
           .getId()
           .equals(user.getId())) {

    throw new IllegalArgumentException(
            "Phone number already registered"
    );
   }

   user.setPhoneNumber(
           r.phoneNumber()
   );
  }

  farmer.setFullName(
          r.fullName()
  );

  farmer.setVillage(
          r.village()
  );

  farmer.setDistrict(
          r.district()
  );

  farmer.setState(
          r.state()
  );

  farmer.setPincode(
          r.pincode()
  );

  farmer.setAlternatePhone(
          r.alternatePhone()
  );

  if (r.active() != null) {

   farmer.setActive(
           r.active()
   );

   user.setActive(
           r.active()
   );
  }

  users.save(user);

  return farmers.save(farmer);
 }

 // GET /api/v1/admin/crops
 @GetMapping("/crops")
 public List<Crop> cropList() {
  return crops.findAll();
 }

 // GET /api/v1/admin/centres
 @GetMapping("/centres")
 public List<ProcurementCentre> centreList() {
  return centres.findAll();
 }

 // GET /api/v1/admin/seasons
 @GetMapping("/seasons")
 public List<ProcurementSeason> seasonList() {
  return seasons.findAll();
 }

 // POST /api/v1/admin/seasons
 @PostMapping("/seasons")
 public ProcurementSeason addSeason(
         @Valid @RequestBody SeasonRequest r
 ) {

  if (r.endDate().isBefore(
          r.startDate()
  )) {

   throw new IllegalArgumentException(
           "Season end date cannot be before start date"
   );
  }

  ProcurementSeason season =
          new ProcurementSeason();

  season.setSeasonName(
          r.seasonName()
  );

  season.setStartDate(
          r.startDate()
  );

  season.setEndDate(
          r.endDate()
  );

  if (r.active() != null) {
   season.setActive(
           r.active()
   );
  }

  return seasons.save(season);
 }

 // GET /api/v1/admin/seasons/{id}/crops
 @GetMapping("/seasons/{id}/crops")
 public List<Crop> seasonCropList(
         @PathVariable Long id
 ) {

  if (!seasons.existsById(id)) {

   throw new NoSuchElementException(
           "Season not found"
   );
  }

  return seasonCrops
          .findActiveCropsBySeasonId(id);
 }

 // GET /api/v1/admin/schedules
 @GetMapping("/schedules")
 public List<Map<String, Object>> scheduleList() {

  List<Map<String, Object>> result =
          new ArrayList<>();

  for (ProcurementSchedule s :
          schedules.findAll()) {

   BigDecimal booked =
           s.getBookedCapacityQuintal();

   if (booked == null) {
    booked = BigDecimal.ZERO;
   }

   BigDecimal available =
           s.getMaxCapacityQuintal()
                   .subtract(booked);

   Map<String, Object> item =
           new LinkedHashMap<>();

   item.put(
           "scheduleId",
           s.getId()
   );

   item.put(
           "centre",
           s.getCentre()
   );

   item.put(
           "season",
           s.getSeason()
   );

   item.put(
           "crop",
           s.getCrop()
   );

   item.put(
           "scheduleDate",
           s.getScheduleDate()
   );

   item.put(
           "startTime",
           s.getStartTime()
   );

   item.put(
           "endTime",
           s.getEndTime()
   );

   item.put(
           "maxCapacityQuintal",
           s.getMaxCapacityQuintal()
   );

   item.put(
           "bookedCapacityQuintal",
           booked
   );

   item.put(
           "availableCapacityQuintal",
           available.max(BigDecimal.ZERO)
   );

   item.put(
           "status",
           s.getStatus()
   );

   result.add(item);
  }

  return result;
 }

 // PATCH /api/v1/admin/bookings/{id}/status
 @PatchMapping("/bookings/{id}/status")
 @Transactional
 public Booking updateBookingStatus(
         @PathVariable Long id,
         @Valid @RequestBody StatusRequest r
 ) {

  Booking booking =
          bookings.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Booking not found"
                          )
                  );

  BookingStatus newStatus =
          parseBookingStatus(
                  r.status()
          );

  booking.setStatus(
          newStatus
  );

  /*
   * Database trigger already present in your schema
   * recalculates booked_capacity_quintal when
   * booking status changes.
   */
  return bookings.saveAndFlush(
          booking
  );
 }

 // GET /api/v1/admin/waiting-list
 @GetMapping("/waiting-list")
 @Transactional(readOnly = true)
 public List<Map<String, Object>> waitingList() {

  List<Map<String, Object>> result =
          new ArrayList<>();

  for (WaitingListEntry w :
          waiting.findAll()) {

   Map<String, Object> item =
           new LinkedHashMap<>();

   item.put(
           "waitingId",
           w.getId()
   );

   item.put(
           "farmerId",
           w.getFarmer().getId()
   );

   item.put(
           "farmerName",
           w.getFarmer().getFullName()
   );

   item.put(
           "scheduleId",
           w.getSchedule().getId()
   );

   item.put(
           "quantityQuintal",
           w.getQuantityQuintal()
   );

   item.put(
           "queuePosition",
           w.getQueuePosition()
   );

   item.put(
           "status",
           w.getStatus()
   );

   item.put(
           "joinedAt",
           w.getJoinedAt()
   );

   result.add(item);
  }

  return result;
 }

 // PATCH /api/v1/admin/waiting-list/{id}/status
 @PatchMapping("/waiting-list/{id}/status")
 public WaitingListEntry updateWaitingStatus(
         @PathVariable Long id,
         @Valid @RequestBody StatusRequest r
 ) {

  WaitingListEntry entry =
          waiting.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Waiting list entry not found"
                          )
                  );

  entry.setStatus(
          parseWaitingStatus(
                  r.status()
          )
  );

  return waiting.save(entry);
 }

 // GET /api/v1/admin/procurement-records
 @GetMapping("/procurement-records")
 public List<ProcurementRecord>
 procurementRecordList() {

  return procurement.findAll();
 }

 // PATCH /api/v1/admin/procurement-records/{id}/status
 @PatchMapping("/procurement-records/{id}/status")
 @Transactional
 public ProcurementRecord updateProcurementStatus(
         @PathVariable Long id,
         @Valid @RequestBody StatusRequest r
 ) {

  ProcurementRecord record =
          procurement.findById(id)
                  .orElseThrow(() ->
                          new NoSuchElementException(
                                  "Procurement record not found"
                          )
                  );

  ProcurementStatus newStatus =
          parseProcurementStatus(
                  r.status()
          );

  record.setStatus(
          newStatus
  );

  /*
   * Keep booking and procurement status consistent.
   */
  if (newStatus ==
          ProcurementStatus.COMPLETED) {

   record.getBooking()
           .setStatus(
                   BookingStatus.COMPLETED
           );

   bookings.saveAndFlush(
           record.getBooking()
   );

   record.setProcuredAt(
           LocalDateTime.now()
   );

  } else if (newStatus ==
          ProcurementStatus.REJECTED) {

   record.getBooking()
           .setStatus(
                   BookingStatus.CANCELLED
           );

   bookings.saveAndFlush(
           record.getBooking()
   );

   record.setProcuredAt(
           LocalDateTime.now()
   );
  }

  return procurement.save(record);
 }

 // GET /api/v1/admin/notifications
 @GetMapping("/notifications")
 public List<Notification>
 notificationList() {

  return notifications.findAll();
 }

 // =========================================================
 // STATUS HELPERS
 // =========================================================

 private BookingStatus parseBookingStatus(
         String value
 ) {

  try {

   return BookingStatus.valueOf(
           value.trim()
                   .toUpperCase(
                           Locale.ROOT
                   )
   );

  } catch (Exception e) {

   throw new IllegalArgumentException(
           "Invalid booking status. Allowed values: " +
                   "PENDING, CONFIRMED, CANCELLED, NO_SHOW, COMPLETED"
   );
  }
 }

 private WaitingStatus parseWaitingStatus(
         String value
 ) {

  try {

   return WaitingStatus.valueOf(
           value.trim()
                   .toUpperCase(
                           Locale.ROOT
                   )
   );

  } catch (Exception e) {

   throw new IllegalArgumentException(
           "Invalid waiting-list status. Allowed values: " +
                   "WAITING, OFFERED, ACCEPTED, EXPIRED, CANCELLED"
   );
  }
 }

 private ProcurementStatus parseProcurementStatus(
         String value
 ) {

  try {

   return ProcurementStatus.valueOf(
           value.trim()
                   .toUpperCase(
                           Locale.ROOT
                   )
   );

  } catch (Exception e) {

   throw new IllegalArgumentException(
           "Invalid procurement status. Allowed values: " +
                   "PENDING, IN_PROGRESS, COMPLETED, REJECTED"
   );
  }
 }
}