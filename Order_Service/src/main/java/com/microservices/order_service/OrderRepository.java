package com.microservices.order_service;

import com.microservices.order_service.model.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderModel, String> {
    List<OrderModel> findByUserId(String userId);
}
