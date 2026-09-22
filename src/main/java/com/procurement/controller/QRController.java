package com.procurement.controller;

import com.procurement.entity.Booking;
import com.procurement.repository.BookingRepository;
import com.procurement.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/v1")
public class QRController {
 private final BookingRepository bookings; private final JwtService jwt;
 public QRController(BookingRepository bookings,JwtService jwt){this.bookings=bookings;this.jwt=jwt;}
 @GetMapping("/qr-pass/{bookingId}") public Map<String,Object> pass(Authentication a,@PathVariable Long bookingId){Booking b=bookings.findById(bookingId).orElseThrow();if(!b.getFarmer().getUser().getPhoneNumber().equals(a.getName()))throw new IllegalArgumentException("Not your booking");return Map.of("bookingId",bookingId,"token",jwt.generate(b.getFarmer().getUser().getId(),"QR-"+bookingId,"QR"),"status",b.getStatus().name(),"date",b.getSchedule().getScheduleDate(),"startTime",b.getSchedule().getStartTime(),"endTime",b.getSchedule().getEndTime());}
 @PostMapping("/operator/qr/verify") public Map<String,Object> verify(@RequestBody Map<String,String> body){String token=body.get("token");if(token==null||!jwt.isValid(token)||!"QR".equals(jwt.extractRole(token)))throw new IllegalArgumentException("Invalid QR token");String subject=jwt.extractPhone(token);if(subject==null||!subject.startsWith("QR-"))throw new IllegalArgumentException("Invalid QR reference");Long id=Long.valueOf(subject.substring(3));Booking b=bookings.findById(id).orElseThrow();if(!(b.getStatus()==com.procurement.entity.BookingStatus.PENDING||b.getStatus()==com.procurement.entity.BookingStatus.CONFIRMED))throw new IllegalArgumentException("Booking is not valid for entry");return Map.of("valid",true,"bookingId",id,"status",b.getStatus().name(),"centreId",b.getSchedule().getCentre().getId(),"scheduleDate",b.getSchedule().getScheduleDate());}
}
