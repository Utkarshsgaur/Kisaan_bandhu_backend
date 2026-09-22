package com.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "procurement_records")
public class ProcurementRecord {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "record_id")
 private Long id;

 @OneToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "booking_id", nullable = false, unique = true)
 private Booking booking;

 @Column(name = "procured_quantity_quintal", precision = 10, scale = 2)
 private BigDecimal procuredQuantityQuintal;

 @Enumerated(EnumType.STRING)
 @Column(name = "procurement_status", nullable = false, length = 20)
 private ProcurementStatus status = ProcurementStatus.PENDING;

 @Column(name = "rejection_reason", length = 255)
 private String rejectionReason;

 @Column(name = "procured_at")
 private LocalDateTime procuredAt;

 @Column(name = "created_at", nullable = false)
 private LocalDateTime createdAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @PrePersist
 void prePersist() {
  createdAt = LocalDateTime.now();
  updatedAt = createdAt;

  if (status == null) {
   status = ProcurementStatus.PENDING;
  }
 }

 @PreUpdate
 void preUpdate() {
  updatedAt = LocalDateTime.now();
 }

 public Long getId() {
  return id;
 }

 public Booking getBooking() {
  return booking;
 }

 public BigDecimal getProcuredQuantityQuintal() {
  return procuredQuantityQuintal;
 }

 public ProcurementStatus getStatus() {
  return status;
 }

 public String getRejectionReason() {
  return rejectionReason;
 }

 public void setBooking(Booking booking) {
  this.booking = booking;
 }

 public void setProcuredQuantityQuintal(BigDecimal procuredQuantityQuintal) {
  this.procuredQuantityQuintal = procuredQuantityQuintal;
 }

 public void setStatus(ProcurementStatus status) {
  this.status = status;
 }

 public void setRejectionReason(String rejectionReason) {
  this.rejectionReason = rejectionReason;
 }

 public void setProcuredAt(LocalDateTime procuredAt) {
  this.procuredAt = procuredAt;
 }
}