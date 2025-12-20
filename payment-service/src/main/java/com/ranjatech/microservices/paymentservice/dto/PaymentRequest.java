package com.ranjatech.microservices.paymentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotBlank
    private String orderId;

    @NotNull
    @Min(1)
    private Double amount;
}

