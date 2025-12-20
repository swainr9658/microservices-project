package com.ranjatech.microservices.paymentservice.service;

import com.ranjatech.microservices.paymentservice.dto.PaymentRequest;
import com.ranjatech.microservices.paymentservice.dto.PaymentResponse;

public interface PaymentService {
	PaymentResponse processPayment(PaymentRequest request);
}
