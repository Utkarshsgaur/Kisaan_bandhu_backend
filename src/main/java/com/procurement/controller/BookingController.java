package com.procurement.controller;

import com.procurement.dto.BookingRequest;
import com.procurement.entity.Booking;
import com.procurement.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService s;

    public BookingController(BookingService s) {
        this.s = s;
    }

    @PostMapping
    public Object create(
            Authentication a,
            @Valid @RequestBody BookingRequest r) {

        return s.create(a.getName(), r);
    }

    @GetMapping("/my")
    public List<Booking> mine(Authentication a) {
        return s.mine(a.getName());
    }

    @GetMapping("/{id}")
    public Booking getById(
            Authentication a,
            @PathVariable Long id) {

        return s.getById(a.getName(), id);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(
            Authentication a,
            @PathVariable Long id) {

        return s.cancel(a.getName(), id);
    }
}