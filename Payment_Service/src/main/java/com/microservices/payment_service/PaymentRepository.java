package com.microservices.payment_service;

import com.microservices.payment_service.model.PaymentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentModel, String> {
    Optional<PaymentModel> findTopByOrderIdOrderByCreatedAtDesc(String orderId);
}
