package com.microservices.order_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${services.product.base-url}")
    private String productBaseUrl;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class ProductResponse {
        public String id;
        public String name;
        public String description;
        public BigDecimal price;
        public Integer stockQuantity;
        public Integer reservedQuantity;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    @Retry(name = "productService")
    public ProductResponse getProduct(String productId) {
        String url = productBaseUrl + "/products/" + productId;
        return restTemplate.getForObject(url, ProductResponse.class);
    }

    public ProductResponse getProductFallback(String productId, Throwable ex) {
        return null; // OrderService will treat this as failure and cancel
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "availabilityFallback")
    @Retry(name = "productService")
    public boolean isAvailable(String productId, int qty) {
        String url = productBaseUrl + "/products/" + productId + "/availability?qty=" + qty;
        Boolean result = restTemplate.getForObject(url, Boolean.class);
        return result != null && result;
    }
    public boolean availabilityFallback(String productId, int qty, Throwable ex) {
        return false;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "reserveFallback")
    @Retry(name = "productService")
    public boolean reserve(String productId, int qty) {
        String url = productBaseUrl + "/products/" + productId + "/reserve?qty=" + qty;
        restTemplate.postForObject(url, null, Object.class);
        return true;
    }
    public boolean reserveFallback(String productId, int qty, Throwable ex) {
        return false;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "releaseFallback")
    @Retry(name = "productService")
    public boolean release(String productId, int qty) {
        String url = productBaseUrl + "/products/" + productId + "/release?qty=" + qty;
        restTemplate.postForObject(url, null, Object.class);
        return true;
    }
    public boolean releaseFallback(String productId, int qty, Throwable ex) {
        return false;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "deductFallback")
    @Retry(name = "productService")
    public boolean deductReserved(String productId, int qty) {
        String url = productBaseUrl + "/products/" + productId + "/deduct-reserved?qty=" + qty;
        restTemplate.postForObject(url, null, Object.class);
        return true;
    }
    public boolean deductFallback(String productId, int qty, Throwable ex) {
        return false;
    }
}
