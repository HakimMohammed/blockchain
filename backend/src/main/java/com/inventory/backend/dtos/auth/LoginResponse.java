package com.inventory.backend.dtos.auth;

import com.inventory.backend.enums.UserRole;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String token;
    private UUID userId;
    private UserRole role;
}
