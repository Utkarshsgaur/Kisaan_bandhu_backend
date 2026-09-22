package com.procurement.controller;

import com.procurement.service.QueueService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {

    private final QueueService s;

    public QueueController(QueueService s) {
        this.s = s;
    }

    @GetMapping("/my/{bookingId}")
    public Map<String, Object> my(
            Authentication a,
            @PathVariable Long bookingId
    ) {
        return s.mine(a.getName(), bookingId);
    }
}