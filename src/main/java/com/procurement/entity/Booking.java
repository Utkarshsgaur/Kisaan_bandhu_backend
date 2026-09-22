package com.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "booking_id")
 private Long id;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "farmer_id", nullable = false)
 private Farmer farmer;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "schedule_id", nullable = false)
 private ProcurementSchedule schedule;

 @Column(
         name = "quantity_quintal",
         nullable = false,
         precision = 10,
         scale = 2
 )
 private BigDecimal quantityQuintal;

 @Enumerated(EnumType.STRING)
 @Column(
         name = "booking_status",
         nullable = false,
         length = 20
 )
 private BookingStatus status = BookingStatus.PENDING;

 @Column(name = "booked_at", nullable = false)
 private LocalDateTime bookedAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @PrePersist
 void prePersist() {
  bookedAt = LocalDateTime.now();
  updatedAt = bookedAt;

  if (status == null) {
   status = BookingStatus.PENDING;
  }
 }

 @PreUpdate
 void preUpdate() {
  updatedAt = LocalDateTime.now();
 }

 public Long getId() {
  return id;
 }

 public Farmer getFarmer() {
  return farmer;
 }

 public ProcurementSchedule getSchedule() {
  return schedule;
 }

 public BigDecimal getQuantityQuintal() {
  return quantityQuintal;
 }

 public BookingStatus getStatus() {
  return status;
 }

 public LocalDateTime getBookedAt() {
  return bookedAt;
 }

 public LocalDateTime getUpdatedAt() {
  return updatedAt;
 }

 public void setFarmer(Farmer v) {
  farmer = v;
 }

 public void setSchedule(ProcurementSchedule v) {
  schedule = v;
 }

 public void setQuantityQuintal(BigDecimal v) {
  quantityQuintal = v;
 }

 public void setStatus(BookingStatus v) {
  status = v;
 }
}