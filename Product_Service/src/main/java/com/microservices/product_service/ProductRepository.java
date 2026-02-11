package com.microservices.product_service;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductModel, String> {
}
