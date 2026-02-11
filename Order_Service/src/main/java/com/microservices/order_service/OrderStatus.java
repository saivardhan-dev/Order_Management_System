package com.microservices.order_service;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    PAYMENT_FAILED,
    CANCELLED
}
