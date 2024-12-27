package com.inventory.backend.dtos.user;

import com.inventory.backend.entities.Organization;
import com.inventory.backend.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Organization organization;
}
