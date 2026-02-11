package com.microservices.order_service;

import com.microservices.order_service.dto.CreateOrderRequest;
import com.microservices.order_service.model.OrderModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderModel> create(@RequestBody @Valid CreateOrderRequest req) {
        return ResponseEntity.ok(service.createOrder(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderModel>> getByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }
}
