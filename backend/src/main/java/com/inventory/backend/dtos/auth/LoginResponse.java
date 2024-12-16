package com.inventory.backend.dtos.auth;

import com.inventory.backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UUID userId;
    private UserRole role;

    // Constructor, getters and setters
}
