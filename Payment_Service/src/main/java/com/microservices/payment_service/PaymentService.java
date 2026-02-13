package com.microservices.payment_service;

import com.microservices.payment_service.dto.PaymentRequest;
import com.microservices.payment_service.dto.PaymentResponse;
import com.microservices.payment_service.model.PaymentModel;
import com.microservices.payment_service.model.PaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    public PaymentResponse process(PaymentRequest req) {
        boolean success;
        String reason = null;

        BigDecimal limit = BigDecimal.valueOf(10000);

        if (req.getForceSuccess() != null) {
            success = req.getForceSuccess();
            if (!success) reason = "Forced failure (test)";
        } else {
            success = req.getAmount() != null
                    && req.getAmount().compareTo(BigDecimal.ZERO) > 0
                    && req.getAmount().compareTo(limit) <= 0;

            if (!success) reason = "Mock rule: amount must be > 0 and <= 10000";
        }

        PaymentModel payment = new PaymentModel();
        payment.setOrderId(req.getOrderId());
        payment.setAmount(req.getAmount());
        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setReason(reason);
        payment.setCreatedAt(Instant.now());

        payment = repo.save(payment);

        PaymentResponse res = new PaymentResponse();
        res.setPaymentId(payment.getId());
        res.setOrderId(payment.getOrderId());
        res.setStatus(payment.getStatus());
        res.setReason(payment.getReason());
        return res;
    }
}
