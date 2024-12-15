package com.inventory.backend.dtos.organization;

import com.inventory.backend.enums.OrganizationType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrganizationResponse {

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
