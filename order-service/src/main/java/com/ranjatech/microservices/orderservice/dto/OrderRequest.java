package com.ranjatech.microservices.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

	@NotBlank
	private String customerId;

	@NotBlank
	private String productId;

	@NotNull
	@Min(1)
	private Integer quantity;

	@NotNull
	@Min(1)
	private Double amount;
}
