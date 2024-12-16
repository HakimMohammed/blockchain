package com.inventory.backend.dtos.auth;

import lombok.Data;

// DTOs (Data Transfer Objects)
@Data
public class LoginRequest {
    private String email;
    private String password;
    // Getters and setters
}
