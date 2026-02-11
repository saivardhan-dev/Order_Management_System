package com.microservices.user_service.Repository;

import com.microservices.user_service.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {
}
