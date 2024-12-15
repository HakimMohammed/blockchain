package com.inventory.backend.dtos.user;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserRequest {

    @Size(max = 25, message = "First name must not exceed 25 characters")
    private String firstName;

    @Size(max = 25, message = "Last name must not exceed 25 characters")
    private String lastName;

    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @Size(max = 20, message = "Password must not exceed 20 characters")
    private String password;

    private String role;
    private UUID organizationId;
}
