package com.microservices.user_service.service;


import com.microservices.user_service.Repository.UserRepository;
import com.microservices.user_service.dto.CreateUserRequest;
import com.microservices.user_service.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserModel create(CreateUserRequest req) {
        UserModel u = new UserModel();
        u.setName(req.getName());
        u.setAddresses(req.getAddresses());
        return repo.save(u);
    }

    public Optional<UserModel> getById(String id) {
        return repo.findById(id);
    }
}
