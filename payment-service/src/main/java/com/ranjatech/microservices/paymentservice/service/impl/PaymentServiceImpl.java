package com.ranjatech.microservices.paymentservice.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ranjatech.microservices.paymentservice.dto.PaymentRequest;
import com.ranjatech.microservices.paymentservice.dto.PaymentResponse;
import com.ranjatech.microservices.paymentservice.entity.Payment;
import com.ranjatech.microservices.paymentservice.repository.PaymentRepository;
import com.ranjatech.microservices.paymentservice.service.PaymentService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StringRedisTemplate redis;

    @Override
    @Retry(name = "paymentRetry")
    @CircuitBreaker(name = "paymentCB", fallbackMethod = "fallbackPayment")
    public PaymentResponse processPayment(PaymentRequest request) {

        // Business rule
        boolean success = request.getAmount() > 0;

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus(success ? "SUCCESS" : "FAILED");

        Payment saved = paymentRepository.save(payment);

        // Cache payment status
        redis.opsForValue()
             .set("payment:" + saved.getOrderId(), saved.getStatus());

        // ✅ Manual mapping (clear & stable)
        return new PaymentResponse(
                saved.getOrderId(),
                saved.getStatus()
        );
    }

    public PaymentResponse fallbackPayment(PaymentRequest req, Throwable ex) {
        return new PaymentResponse(req.getOrderId(), "FAILED");
    }
}
