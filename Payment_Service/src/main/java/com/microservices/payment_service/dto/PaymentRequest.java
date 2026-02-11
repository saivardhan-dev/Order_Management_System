package com.microservices.payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotBlank
    private String orderId;

    @NotNull
    private BigDecimal amount;

    // mock behavior: if true -> force success, if false -> force fail, if null -> automatic rule
    private Boolean forceSuccess;

    public PaymentRequest(String orderId, @NotNull BigDecimal amount, Boolean forceSuccess) {
        this.orderId = orderId;
        this.amount = amount;
        this.forceSuccess = forceSuccess;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @NotNull
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getForceSuccess() {
        return forceSuccess;
    }

    public void setForceSuccess(Boolean forceSuccess) {
        this.forceSuccess = forceSuccess;
    }
}