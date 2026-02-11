package com.microservices.payment_service;

import com.microservices.payment_service.dto.PaymentRequest;
import com.microservices.payment_service.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid PaymentRequest req) {
        return ResponseEntity.ok(service.process(req));
    }
}
