package com.microservices.order_service.model;

import com.microservices.order_service.OrderItem;
import com.microservices.order_service.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
public class OrderModel {

    @Id
    private String id;

    private String userId;
    private List<OrderItem> items;

    private BigDecimal totalAmount;
    private OrderStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    private OrderAddress shippingAddress;

    public OrderModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt;}

    public OrderAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(OrderAddress shippingAddress) { this.shippingAddress = shippingAddress;}
}
