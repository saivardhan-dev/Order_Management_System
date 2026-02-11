package com.microservices.product_service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ProductModel> create(@RequestBody @Valid CreateProductRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Used by order-service to check availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> availability(
            @PathVariable String id,
            @RequestParam int qty
    ) {
        return ResponseEntity.ok(service.isAvailable(id, qty));
    }

    // Used after payment success (later we’ll call this)
    @PostMapping("/{id}/deduct")
    public ResponseEntity<ProductModel> deduct(
            @PathVariable String id,
            @RequestParam int qty
    ) {
        return ResponseEntity.ok(service.deductStock(id, qty));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ProductModel> reserve(@PathVariable String id, @RequestParam int qty) {
        return ResponseEntity.ok(service.reserveStock(id, qty));
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<ProductModel> release(@PathVariable String id, @RequestParam int qty) {
        return ResponseEntity.ok(service.releaseReservation(id, qty));
    }

    @PostMapping("/{id}/deduct-reserved")
    public ResponseEntity<ProductModel> deductReserved(@PathVariable String id, @RequestParam int qty) {
        return ResponseEntity.ok(service.deductReservedStock(id, qty));
    }
}