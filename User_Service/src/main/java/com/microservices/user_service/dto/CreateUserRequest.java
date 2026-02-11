package com.microservices.user_service.dto;

import com.microservices.user_service.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class CreateUserRequest {
    @NotBlank
    private String name;

    @Valid
    private List<Address> addresses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
