package com.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "notification_id")
 private Long id;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "farmer_id", nullable = false)
 private Farmer farmer;

 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "booking_id")
 private Booking booking;

 @Enumerated(EnumType.STRING)
 @Column(name = "notification_type", nullable = false, length = 30)
 private NotificationType type;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 10)
 private NotificationChannel channel;

 @Column(nullable = false, columnDefinition = "text")
 private String message;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 10)
 private NotificationStatus status = NotificationStatus.PENDING;

 @Column(name = "sent_at")
 private LocalDateTime sentAt;

 @Column(name = "created_at", nullable = false)
 private LocalDateTime createdAt;

 @PrePersist
 void prePersist() {
  createdAt = LocalDateTime.now();
 }

 public Long getId() {
  return id;
 }

 public Farmer getFarmer() {
  return farmer;
 }

 public Booking getBooking() {
  return booking;
 }

 public NotificationType getType() {
  return type;
 }

 public NotificationChannel getChannel() {
  return channel;
 }

 public String getMessage() {
  return message;
 }

 public NotificationStatus getStatus() {
  return status;
 }

 public LocalDateTime getCreatedAt() {
  return createdAt;
 }

 public LocalDateTime getSentAt() {
  return sentAt;
 }

 public void setFarmer(Farmer farmer) {
  this.farmer = farmer;
 }

 public void setBooking(Booking booking) {
  this.booking = booking;
 }

 public void setType(NotificationType type) {
  this.type = type;
 }

 public void setChannel(NotificationChannel channel) {
  this.channel = channel;
 }

 public void setMessage(String message) {
  this.message = message;
 }

 public void setStatus(NotificationStatus status) {
  this.status = status;
 }

 public void setSentAt(LocalDateTime sentAt) {
  this.sentAt = sentAt;
 }
}