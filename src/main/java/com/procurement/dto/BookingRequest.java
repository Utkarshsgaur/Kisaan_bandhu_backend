package com.procurement.dto; import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record BookingRequest(@NotNull Long scheduleId,@NotNull @DecimalMin("0.01") BigDecimal quantityQuintal){}
