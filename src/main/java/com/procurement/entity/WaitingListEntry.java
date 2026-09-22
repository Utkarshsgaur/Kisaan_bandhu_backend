package com.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
public class WaitingListEntry {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "waiting_id")
 private Long id;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "farmer_id", nullable = false)
 private Farmer farmer;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "schedule_id", nullable = false)
 private ProcurementSchedule schedule;

 @Column(
         name = "quantity_quintal",
         nullable = false,
         precision = 10,
         scale = 2
 )
 private BigDecimal quantityQuintal;

 @Column(
         name = "queue_position",
         nullable = false
 )
 private Integer queuePosition;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private WaitingStatus status = WaitingStatus.WAITING;

 @Column(name = "joined_at", nullable = false)
 private LocalDateTime joinedAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @PrePersist
 void prePersist() {
  joinedAt = LocalDateTime.now();
  updatedAt = joinedAt;

  if (status == null) {
   status = WaitingStatus.WAITING;
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

 public Integer getQueuePosition() {
  return queuePosition;
 }

 public WaitingStatus getStatus() {
  return status;
 }

 // ✅ Missing getter added
 public LocalDateTime getJoinedAt() {
  return joinedAt;
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

 public void setQueuePosition(Integer v) {
  queuePosition = v;
 }

 public void setStatus(WaitingStatus v) {
  status = v;
 }
}