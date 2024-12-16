package com.inventory.backend.dtos.auth;

import com.inventory.backend.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private UUID organizationId;
    // Getters and setters
}
