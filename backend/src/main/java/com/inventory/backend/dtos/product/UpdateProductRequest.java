package com.inventory.backend.dtos.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProductRequest {

    @Size(max = 25, message = "Name must not exceed 25 characters")
    private String name;

    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    private String image;

    private UUID categoryId;

    private UUID organizationId;
}
