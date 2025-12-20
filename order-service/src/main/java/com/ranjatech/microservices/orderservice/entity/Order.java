package com.ranjatech.microservices.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true, updatable = false)
	private String orderNumber; // UUID string

	@Column(nullable = false)
	private String customerId;

	@Column(nullable = false)
	private String productId;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private String status; // CREATED, PAYMENT_PENDING, COMPLETED, FAILED

	@Column(nullable = false)
	private Boolean active = true;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		if (orderNumber == null) {
			orderNumber = UUID.randomUUID().toString();
		}
		if (status == null) {
			status = "CREATED";
		}
	}
}
