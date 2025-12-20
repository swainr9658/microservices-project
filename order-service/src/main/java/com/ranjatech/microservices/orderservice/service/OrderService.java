package com.ranjatech.microservices.orderservice.service;

import com.ranjatech.microservices.orderservice.dto.OrderRequest;
import com.ranjatech.microservices.orderservice.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderByNumber(String orderNumber);

    void cancelOrder(String orderNumber);
}
