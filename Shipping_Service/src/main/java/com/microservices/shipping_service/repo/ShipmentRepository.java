package com.microservices.shipping_service.repo;

import com.microservices.shipping_service.model.ShipmentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShipmentRepository extends MongoRepository<ShipmentModel, String> {
    Optional<ShipmentModel> findByOrderId(String orderId);
}