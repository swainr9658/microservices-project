package com.ranjatech.microservices.orderservice.service.impl;

import com.ranjatech.microservices.orderservice.client.PaymentClient;
import com.ranjatech.microservices.orderservice.dto.*;
import com.ranjatech.microservices.orderservice.entity.Order;
import com.ranjatech.microservices.orderservice.exception.BusinessException;
import com.ranjatech.microservices.orderservice.exception.ResourceNotFoundException;
import com.ranjatech.microservices.orderservice.repository.OrderRepository;
import com.ranjatech.microservices.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    @Retry(name = "paymentService")
    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackCreateOrder")
    public OrderResponse createOrder(OrderRequest request) {

        // 1. Simple validation/business rules
        if (request.getAmount() <= 0 || request.getQuantity() <= 0) {
            throw new BusinessException("Amount and quantity must be greater than zero");
        }

        // 2. Persist order with CREATED/PAYMENT_PENDING status
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setStatus("PAYMENT_PENDING");

        order = orderRepository.save(order);

        // 3. Call payment service
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderNumber())
                .amount(order.getAmount())
                .build();

        PaymentResponse paymentResponse = paymentClient.makePayment(paymentRequest);

        if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
            order.setStatus("COMPLETED");
        } else {
            order.setStatus("FAILED");
            throw new BusinessException("Payment failed for order " + order.getOrderNumber());
        }

        orderRepository.save(order);

        // 4. Return DTO
        return mapToResponse(order);
    }

    // Fallback when payment service is down
    private OrderResponse fallbackCreateOrder(OrderRequest request, Throwable ex) {
        log.error("Payment service unavailable, creating order with status=PAYMENT_PENDING", ex);
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setStatus("PAYMENT_PENDING");
        order = orderRepository.save(order);
        return mapToResponse(order);
    }

    @Override
    @Cacheable(value = "orders", key = "#orderNumber")
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#orderNumber")
    public void cancelOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumberAndActiveTrue(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));

        if ("COMPLETED".equalsIgnoreCase(order.getStatus())) {
            throw new BusinessException("Cannot cancel a completed order");
        }

        order.setActive(false);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
