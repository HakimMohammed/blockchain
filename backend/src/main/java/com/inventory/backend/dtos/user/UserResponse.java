package com.inventory.backend.dtos.user;

import com.inventory.backend.enums.UserRole;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private String organizationName;
}
