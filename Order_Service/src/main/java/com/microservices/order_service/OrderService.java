package com.microservices.order_service;

import com.microservices.order_service.client.PaymentClient;
import com.microservices.order_service.client.ProductClient;
import com.microservices.order_service.dto.CreateOrderRequest;
import com.microservices.order_service.model.OrderAddress;
import com.microservices.order_service.model.OrderModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository repo, ProductClient productClient, PaymentClient paymentClient) {
        this.repo = repo;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    public OrderModel createOrder(CreateOrderRequest req) {

        OrderModel order = new OrderModel();
        order.setUserId(req.getUserId());

        List<OrderItem> items = req.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getQuantity()))
                .toList();
        order.setItems(items);

        // map shipping snapshot
        CreateOrderRequest.ShippingAddress reqAddr = req.getShippingAddress();
        OrderAddress addr = new OrderAddress();
        addr.setHouseNum(reqAddr.getHouseNum());
        addr.setCity(reqAddr.getCity());
        addr.setState(reqAddr.getState());
        addr.setZip(reqAddr.getZip());
        addr.setCountry(reqAddr.getCountry());
        order.setShippingAddress(addr);

        Instant now = Instant.now();
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        order = repo.save(order);

        // 1) availability check
        boolean allAvailable = items.stream().allMatch(i ->
                productClient.isAvailable(i.getProductId(), safeQty(i.getQuantity()))
        );

        if (!allAvailable) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(Instant.now());
            return repo.save(order);
        }

        // 2) reserve stock for all items
        // track successfully reserved items so we can roll back if needed
        List<OrderItem> reservedItems = new java.util.ArrayList<>();
        for (OrderItem item : items) {
            boolean reserved = productClient.reserve(item.getProductId(), safeQty(item.getQuantity()));
            if (!reserved) {
                // rollback (release whatever was reserved)
                for (OrderItem r : reservedItems) {
                    productClient.release(r.getProductId(), safeQty(r.getQuantity()));
                }
                order.setStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(Instant.now());
                return repo.save(order);
            }
            reservedItems.add(item);
        }

        // 3) payment pending
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order.setUpdatedAt(Instant.now());

        BigDecimal amount = calculateTotalFromProducts(items);
        order.setTotalAmount(amount);
        order = repo.save(order);

        // 4) call payment
        PaymentClient.PaymentResponse paymentRes = paymentClient.pay(order.getId(), amount);
        boolean paymentSuccess = paymentRes != null
                && paymentRes.status != null
                && "SUCCESS".equalsIgnoreCase(paymentRes.status);

        if (paymentSuccess) {
            // 5) deduct reserved stock permanently
            boolean allDeducted = true;
            for (OrderItem item : items) {
                boolean deducted = productClient.deductReserved(item.getProductId(), safeQty(item.getQuantity()));
                if (!deducted) {
                    allDeducted = false;
                    break;
                }
            }

            if (allDeducted) {
                order.setStatus(OrderStatus.PAID);
            } else {
                for (OrderItem item : items) {
                    productClient.release(item.getProductId(), safeQty(item.getQuantity()));
                }
                order.setStatus(OrderStatus.CANCELLED);
            }
        } else {
            // payment failed: release reservation
            for (OrderItem item : items) {
                productClient.release(item.getProductId(), safeQty(item.getQuantity()));
            }
            order.setStatus(OrderStatus.CANCELLED);
        }

        order.setUpdatedAt(Instant.now());
        return repo.save(order);
    }

    private int safeQty(Integer qty) {
        return (qty == null || qty < 0) ? 0 : qty;
    }

    private BigDecimal calculateTotalFromProducts(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;

        // fetch each product only once
        Map<String, ProductClient.ProductResponse> cache = new HashMap<>();

        for (OrderItem item : items) {
            String pid = item.getProductId();
            int qty = safeQty(item.getQuantity());

            if (qty <= 0) continue;

            ProductClient.ProductResponse p = cache.computeIfAbsent(pid, productClient::getProduct);

            if (p == null || p.price == null) {
                throw new RuntimeException("Unable to fetch price for productId=" + pid);
            }

            total = total.add(p.price.multiply(BigDecimal.valueOf(qty)));
        }

        return total;
    }

    public Optional<OrderModel> getById(String id) {
        return repo.findById(id);
    }

    public List<OrderModel> getByUserId(String userId) {
        return repo.findByUserId(userId);
    }
}