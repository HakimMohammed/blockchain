package com.inventory.backend.dtos.category;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCategoryRequest {

    @Size(max = 25, message = "Name must not exceed 25 characters")
    private String name;

    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;
}
