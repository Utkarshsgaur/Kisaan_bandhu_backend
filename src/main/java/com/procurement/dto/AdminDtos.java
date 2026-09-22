package com.procurement.dto;

import com.procurement.entity.ScheduleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public final class AdminDtos {

 private AdminDtos() {
 }

 // =========================================================
 // EXISTING PHOTO API DTOs
 // =========================================================

 public record CropRequest(
         @NotBlank String cropName,
         @NotBlank String cropCode,
         Boolean active
 ) {
 }

 public record CentreRequest(
         @NotBlank String centreCode,
         @NotBlank String centreName,
         @NotBlank String district,
         @NotBlank String state,
         @NotBlank String address,
         @Pattern(regexp = "[0-9]{6}") String pincode,
         String contactPhone,
         Boolean active
 ) {
 }

 public record ScheduleRequest(
         @NotNull Long centreId,
         @NotNull Long seasonId,
         @NotNull Long cropId,
         @NotNull LocalDate scheduleDate,
         @NotNull LocalTime startTime,
         @NotNull LocalTime endTime,
         @NotNull @DecimalMin("0.01") BigDecimal maxCapacityQuintal,
         ScheduleStatus status
 ) {
 }

 // =========================================================
 // NEW ADMIN FARMER API DTO
 // =========================================================

 public record FarmerRequest(
         @NotBlank
         @Size(max = 15)
         String phoneNumber,

         @NotBlank
         @Size(max = 100)
         String fullName,

         @NotBlank
         @Size(max = 100)
         String village,

         @NotBlank
         @Size(max = 100)
         String district,

         @NotBlank
         @Size(max = 100)
         String state,

         @NotBlank
         @Pattern(regexp = "[0-9]{6}")
         String pincode,

         @Size(max = 15)
         String alternatePhone,

         Boolean active
 ) {
 }

 // =========================================================
 // NEW ADMIN SEASON API DTO
 // =========================================================

 public record SeasonRequest(
         @NotBlank
         @Size(max = 100)
         String seasonName,

         @NotNull
         LocalDate startDate,

         @NotNull
         LocalDate endDate,

         Boolean active
 ) {
 }

 // =========================================================
 // NEW STATUS API DTO
 // =========================================================

 public record StatusRequest(
         @NotBlank
         String status
 ) {
 }
}