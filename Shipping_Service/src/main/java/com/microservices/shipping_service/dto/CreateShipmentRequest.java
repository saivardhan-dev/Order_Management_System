package com.microservices.shipping_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateShipmentRequest {

    @NotBlank
    private String orderId;

    @NotBlank
    private String userId;

    @NotNull
    @Valid
    private ShippingAddress shippingAddress;

    public static class ShippingAddress {
        @NotBlank private String houseNum;
        @NotBlank private String city;
        @NotBlank private String state;
        @NotBlank private String zip;
        @NotBlank private String country;

        public String getHouseNum() { return houseNum; }
        public void setHouseNum(String houseNum) { this.houseNum = houseNum; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getZip() { return zip; }
        public void setZip(String zip) { this.zip = zip; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }
}