package com.ranjatech.microservices.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranjatech.microservices.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}