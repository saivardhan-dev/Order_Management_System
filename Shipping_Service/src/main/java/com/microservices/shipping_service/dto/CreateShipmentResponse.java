package com.microservices.shipping_service.dto;

import com.microservices.shipping_service.model.ShipmentStatus;

public class CreateShipmentResponse {
    private String trackingId;
    private String orderId;
    private String userId;
    private ShipmentStatus status;

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
}