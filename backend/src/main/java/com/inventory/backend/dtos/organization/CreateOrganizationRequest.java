package com.inventory.backend.dtos.organization;

import com.inventory.backend.enums.OrganizationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrganizationRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 25, message = "Name must not exceed 25 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone must not exceed 15 characters")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 50, message = "Address must not exceed 50 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 25, message = "City must not exceed 25 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 25, message = "State must not exceed 25 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 25, message = "Country must not exceed 25 characters")
    private String country;

    @NotBlank(message = "ZIP code is required")
    @Size(max = 10, message = "ZIP code must not exceed 10 characters")
    private String zip;

    @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    private OrganizationType type;
}

