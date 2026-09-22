package com.procurement.controller;
import com.procurement.entity.ProcurementRecord; import com.procurement.service.ProcurementService; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/procurement") public class ProcurementStatusController {private final ProcurementService s;public ProcurementStatusController(ProcurementService s){this.s=s;} @GetMapping("/{bookingId}/status") public ProcurementRecord status(Authentication a,@PathVariable Long bookingId){return s.status(bookingId);}}
