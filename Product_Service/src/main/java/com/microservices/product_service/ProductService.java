package com.microservices.product_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public ProductModel create(CreateProductRequest req) {
        ProductModel p = new ProductModel();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStockQuantity(req.getStockQuantity());
        p.setReservedQuantity(0); // ✅ important default
        return repo.save(p);
    }

    // ✅ FIX: return List/Iterable, not Optional<ProductModel>
    public List<ProductModel> getAll() {
        return repo.findAll();
    }

    public Optional<ProductModel> getById(String id) {
        return repo.findById(id);
    }

    // ✅ Availability should consider reservedQuantity
    public boolean isAvailable(String productId, int qty) {
        return repo.findById(productId)
                .map(p -> {
                    int stock = p.getStockQuantity() == null ? 0 : p.getStockQuantity();
                    int reserved = p.getReservedQuantity() == null ? 0 : p.getReservedQuantity();
                    return (stock - reserved) >= qty;
                })
                .orElse(false);
    }

    // ✅ Reserve inventory (temporary hold)
    public ProductModel reserveStock(String productId, int qty) {
        ProductModel p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int stock = p.getStockQuantity() == null ? 0 : p.getStockQuantity();
        int reserved = p.getReservedQuantity() == null ? 0 : p.getReservedQuantity();
        int available = stock - reserved;

        if (available < qty) {
            throw new RuntimeException("Insufficient available stock to reserve");
        }

        p.setReservedQuantity(reserved + qty);
        return repo.save(p);
    }

    // ✅ Release reservation (on payment fail/cancel)
    public ProductModel releaseReservation(String productId, int qty) {
        ProductModel p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int reserved = p.getReservedQuantity() == null ? 0 : p.getReservedQuantity();
        p.setReservedQuantity(Math.max(0, reserved - qty));

        return repo.save(p);
    }

    // ✅ Deduct permanently after payment success
    public ProductModel deductReservedStock(String productId, int qty) {
        ProductModel p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int stock = p.getStockQuantity() == null ? 0 : p.getStockQuantity();
        int reserved = p.getReservedQuantity() == null ? 0 : p.getReservedQuantity();

        if (reserved < qty) {
            throw new RuntimeException("Not enough reserved stock to deduct");
        }
        if (stock < qty) {
            throw new RuntimeException("Stock is unexpectedly less than deduction qty");
        }

        p.setReservedQuantity(reserved - qty);
        p.setStockQuantity(stock - qty);

        return repo.save(p);
    }

    // (Optional) old method - not used in reserve flow anymore, but can keep for admin
    public ProductModel deductStock(String productId, int qty) {
        ProductModel p = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int current = (p.getStockQuantity() == null) ? 0 : p.getStockQuantity();
        if (current < qty) {
            throw new RuntimeException("Insufficient stock");
        }

        p.setStockQuantity(current - qty);
        return repo.save(p);
    }
}