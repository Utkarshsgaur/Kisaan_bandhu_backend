package com.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "procurement_centres")
public class ProcurementCentre {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "centre_id")
 private Long id;

 @Column(name = "centre_code", nullable = false, unique = true, length = 20)
 private String centreCode;

 @Column(name = "centre_name", nullable = false, length = 150)
 private String centreName;

 @Column(nullable = false, length = 100)
 private String district;

 @Column(nullable = false, length = 100)
 private String state;

 @Column(nullable = false, length = 255)
 private String address;

 // Database me pincode CHAR(6) hai
 @Column(name = "pincode", nullable = false, columnDefinition = "char(6)")
 private String pincode;

 @Column(name = "contact_phone", length = 15)
 private String contactPhone;

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

 public String getCentreCode() {
  return centreCode;
 }

 public void setCentreCode(String v) {
  centreCode = v;
 }

 public String getCentreName() {
  return centreName;
 }

 public void setCentreName(String v) {
  centreName = v;
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

 public String getAddress() {
  return address;
 }

 public void setAddress(String v) {
  address = v;
 }

 public String getPincode() {
  return pincode;
 }

 public void setPincode(String v) {
  pincode = v;
 }

 public String getContactPhone() {
  return contactPhone;
 }

 public void setContactPhone(String v) {
  contactPhone = v;
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