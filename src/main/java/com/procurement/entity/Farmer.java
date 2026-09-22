package com.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmers")
public class Farmer {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "farmer_id")
 private Long id;

 @OneToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "user_id", nullable = false, unique = true)
 private User user;

 @Column(name = "full_name", nullable = false, length = 100)
 private String fullName;

 @Column(nullable = false, length = 100)
 private String village;

 @Column(nullable = false, length = 100)
 private String district;

 @Column(nullable = false, length = 100)
 private String state;

 // Database me pincode CHAR(6) hai
 @Column(
         name = "pincode",
         nullable = false,
         columnDefinition = "char(6)"
 )
 private String pincode;

 @Column(name = "alternate_phone", length = 15)
 private String alternatePhone;

 // Database me aadhaar_hash CHAR(64) hai
 @Column(
         name = "aadhaar_hash",
         columnDefinition = "char(64)",
         unique = true
 )
 private String aadhaarHash;

 @Column(name = "is_active", nullable = false)
 private boolean active = true;

 @Column(name = "created_at", nullable = false)
 private LocalDateTime createdAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @PrePersist
 void prePersist() {
  createdAt = LocalDateTime.now();
  updatedAt = createdAt;
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

 public User getUser() {
  return user;
 }

 public void setUser(User v) {
  user = v;
 }

 public String getFullName() {
  return fullName;
 }

 public void setFullName(String v) {
  fullName = v;
 }

 public String getVillage() {
  return village;
 }

 public void setVillage(String v) {
  village = v;
 }

 public String getDistrict() {
  return district;
 }

 public void setDistrict(String v) {
  district = v;
 }

 public String getState() {
  return state;
 }

 public void setState(String v) {
  state = v;
 }

 public String getPincode() {
  return pincode;
 }

 public void setPincode(String v) {
  pincode = v;
 }

 public String getAlternatePhone() {
  return alternatePhone;
 }

 public void setAlternatePhone(String v) {
  alternatePhone = v;
 }

 public String getAadhaarHash() {
  return aadhaarHash;
 }

 public void setAadhaarHash(String v) {
  aadhaarHash = v;
 }

 public boolean isActive() {
  return active;
 }

 public void setActive(boolean v) {
  active = v;
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