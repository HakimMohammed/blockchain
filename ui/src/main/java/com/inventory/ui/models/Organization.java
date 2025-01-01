package com.inventory.ui.models;

import com.inventory.ui.enums.OrganizationType;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String description;
    private OrganizationType type;
}

