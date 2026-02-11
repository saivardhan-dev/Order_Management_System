package com.microservices.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotBlank
    private String userId;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    @NotNull
    @Valid
    private ShippingAddress shippingAddress;

    // ===== getters & setters =====

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    // ===== Inner Classes =====

    public static class OrderItemRequest {

        @NotBlank
        private String productId;

        @NotNull
        private Integer quantity;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class ShippingAddress {

        @NotBlank
        private String houseNum;

        @NotBlank
        private String city;

        @NotBlank
        private String state;

        @NotBlank
        private String zip;

        @NotBlank
        private String country;

        public String getHouseNum() {
            return houseNum;
        }

        public void setHouseNum(String houseNum) {
            this.houseNum = houseNum;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}