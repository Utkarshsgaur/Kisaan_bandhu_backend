package com.procurement.controller;

import com.procurement.dto.AuthDtos.*;
import com.procurement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService s;

    public AuthController(AuthService s) {
        this.s = s;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest r
    ) {
        return s.register(r);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest r
    ) {
        return s.login(r);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> send(
            @Valid @RequestBody OtpRequest r
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP generated",
                        "otp",
                        s.sendOtp(r)
                )
        );
    }

    @PostMapping("/verify-otp")
    public AuthResponse verify(
            @Valid @RequestBody VerifyOtpRequest r
    ) {
        return s.verifyOtp(r);
    }

    /**
     * Health check endpoint for deployment platforms (Render, etc.)
     * Public — no authentication required.
     */
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of("status", "ok", "service", "kisan-bandhu-backend"));
    }
}