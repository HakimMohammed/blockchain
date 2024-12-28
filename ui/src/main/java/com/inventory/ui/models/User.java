package com.inventory.ui.models;

import com.inventory.ui.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Organization organization;
}
