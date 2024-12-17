package com.inventory.backend.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 25, message = "Name must not exceed 25 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;
}