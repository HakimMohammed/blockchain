package com.inventory.backend.dtos.organization;

import com.inventory.backend.enums.OrganizationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateOrganizationRequest {

    @Size(max = 25, message = "Name must not exceed 25 characters")
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @Size(max = 15, message = "Phone must not exceed 15 characters")
    private String phone;

    @Size(max = 50, message = "Address must not exceed 50 characters")
    private String address;

    @Size(max = 25, message = "City must not exceed 25 characters")
    private String city;

    @Size(max = 25, message = "State must not exceed 25 characters")
    private String state;

    @Size(max = 25, message = "Country must not exceed 25 characters")
    private String country;

    @Size(max = 10, message = "ZIP code must not exceed 10 characters")
    private String zip;

    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    private OrganizationType type;
}

