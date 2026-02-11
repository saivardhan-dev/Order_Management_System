package com.microservices.order_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class PaymentClient {

    private final RestTemplate restTemplate;

    @Value("${services.payment.base-url}")
    private String paymentBaseUrl;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // DTOs inside client to avoid sharing modules for now
    public static class PaymentRequest {
        public String orderId;
        public BigDecimal amount;
        public Boolean forceSuccess; // optional for testing
    }

    public static class PaymentResponse {
        public String paymentId;
        public String orderId;
        public String status; // "SUCCESS" / "FAILED"
        public String reason;
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "payFallback")
    @Retry(name = "paymentService")
    public PaymentResponse pay(String orderId, BigDecimal amount) {
        String url = paymentBaseUrl + "/payments";
        PaymentRequest req = new PaymentRequest();
        req.orderId = orderId;
        req.amount = amount;
        return restTemplate.postForObject(url, req, PaymentResponse.class);
    }

    public PaymentResponse payFallback(String orderId, BigDecimal amount, Throwable ex) {
        PaymentResponse res = new PaymentResponse();
        res.orderId = orderId;
        res.status = "FAILED";
        res.reason = "payment-service unavailable (fallback)";
        return res;
    }
}