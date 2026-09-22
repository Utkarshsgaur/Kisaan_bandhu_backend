package com.procurement.dto; import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record ProcurementRequest(@NotNull @DecimalMin("0.01") BigDecimal quantityQuintal,String rejectionReason){}
