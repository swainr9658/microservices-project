package com.ranjatech.microservices.orderservice.client;

import com.ranjatech.microservices.orderservice.dto.PaymentRequest;
import com.ranjatech.microservices.orderservice.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse makePayment(PaymentRequest request);
}
