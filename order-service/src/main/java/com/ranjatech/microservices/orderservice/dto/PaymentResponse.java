package com.ranjatech.microservices.orderservice.dto;

import lombok.Data;

@Data
public class PaymentResponse {
	private String status; // SUCCESS / FAILED
	private String txnId;
}
