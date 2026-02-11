package com.microservices.shipping_service;

import com.microservices.shipping_service.dto.CreateShipmentRequest;
import com.microservices.shipping_service.dto.CreateShipmentResponse;
import com.microservices.shipping_service.model.ShipmentModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
public class ShippingController {

    private final ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreateShipmentResponse> create(@RequestBody @Valid CreateShipmentRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<ShipmentModel> getByTrackingId(@PathVariable String trackingId) {
        return service.getByTrackingId(trackingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}