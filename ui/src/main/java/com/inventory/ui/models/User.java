package com.inventory.ui.models;

import com.inventory.ui.enums.Role;
import com.inventory.ui.models.Organization;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

    private Organization organization;


}
