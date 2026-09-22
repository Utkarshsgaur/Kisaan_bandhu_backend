package com.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "procurement_schedules")
public class ProcurementSchedule {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "schedule_id")
 private Long id;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "centre_id", nullable = false)
 private ProcurementCentre centre;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "season_id", nullable = false)
 private ProcurementSeason season;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "crop_id", nullable = false)
 private Crop crop;

 @Column(name = "schedule_date", nullable = false)
 private LocalDate scheduleDate;

 @Column(name = "start_time", nullable = false)
 private LocalTime startTime;

 @Column(name = "end_time", nullable = false)
 private LocalTime endTime;

 @Column(
         name = "max_capacity_quintal",
         nullable = false,
         precision = 10,
         scale = 2
 )
 private BigDecimal maxCapacityQuintal;

 @Column(
         name = "booked_capacity_quintal",
         nullable = false,
         precision = 10,
         scale = 2
 )
 private BigDecimal bookedCapacityQuintal = BigDecimal.ZERO;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private ScheduleStatus status = ScheduleStatus.OPEN;

 @Column(name = "created_at", nullable = false)
 private LocalDateTime createdAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @PrePersist
 void prePersist() {
  createdAt = LocalDateTime.now();
  updatedAt = createdAt;

  if (bookedCapacityQuintal == null) {
   bookedCapacityQuintal = BigDecimal.ZERO;
  }

  if (status == null) {
   status = ScheduleStatus.OPEN;
  }
 }

 @PreUpdate
 void preUpdate() {
  updatedAt = LocalDateTime.now();
 }

 public Long getId() {
  return id;
 }

 public void setId(Long v) {
  id = v;
 }

 public ProcurementCentre getCentre() {
  return centre;
 }

 public void setCentre(ProcurementCentre v) {
  centre = v;
 }

 public ProcurementSeason getSeason() {
  return season;
 }

 public void setSeason(ProcurementSeason v) {
  season = v;
 }

 public Crop getCrop() {
  return crop;
 }

 public void setCrop(Crop v) {
  crop = v;
 }

 public LocalDate getScheduleDate() {
  return scheduleDate;
 }

 public void setScheduleDate(LocalDate v) {
  scheduleDate = v;
 }

 public LocalTime getStartTime() {
  return startTime;
 }

 public void setStartTime(LocalTime v) {
  startTime = v;
 }

 public LocalTime getEndTime() {
  return endTime;
 }

 public void setEndTime(LocalTime v) {
  endTime = v;
 }

 public BigDecimal getMaxCapacityQuintal() {
  return maxCapacityQuintal;
 }

 public void setMaxCapacityQuintal(BigDecimal v) {
  maxCapacityQuintal = v;
 }

 public BigDecimal getBookedCapacityQuintal() {
  return bookedCapacityQuintal;
 }

 public void setBookedCapacityQuintal(BigDecimal v) {
  bookedCapacityQuintal = v;
 }

 public ScheduleStatus getStatus() {
  return status;
 }

 public void setStatus(ScheduleStatus v) {
  status = v;
 }

 public LocalDateTime getCreatedAt() {
  return createdAt;
 }

 public void setCreatedAt(LocalDateTime v) {
  createdAt = v;
 }

 public LocalDateTime getUpdatedAt() {
  return updatedAt;
 }

 public void setUpdatedAt(LocalDateTime v) {
  updatedAt = v;
 }
}