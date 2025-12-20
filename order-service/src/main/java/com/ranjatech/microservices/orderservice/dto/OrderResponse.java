package com.ranjatech.microservices.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {

	private String orderNumber;
	private String customerId;
	private String productId;
	private Integer quantity;
	private Double amount;
	private String status;
	private LocalDateTime createdAt;
	
}
