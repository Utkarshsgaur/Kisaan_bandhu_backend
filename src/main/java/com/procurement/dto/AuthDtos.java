package com.procurement.dto;
import jakarta.validation.constraints.*;
public final class AuthDtos {
 private AuthDtos(){}
 public record RegisterRequest(@NotBlank @Size(max=15) String phoneNumber,@NotBlank @Size(max=100) String fullName,@NotBlank String village,@NotBlank String district,@NotBlank String state,@Pattern(regexp="[0-9]{6}") String pincode,@Size(max=15) String alternatePhone){}
 public record LoginRequest(@NotBlank String phoneNumber){}
 public record OtpRequest(@NotBlank String phoneNumber){}
 public record VerifyOtpRequest(@NotBlank String phoneNumber,@NotBlank String otp){}
 public record AuthResponse(String token,Long userId,Long farmerId,String role,String fullName){}
}
