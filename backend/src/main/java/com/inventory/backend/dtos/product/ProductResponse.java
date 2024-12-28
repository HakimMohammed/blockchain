package com.inventory.backend.dtos.product;

import com.inventory.backend.dtos.category.CategoryResponse;
import com.inventory.backend.entities.Category;
import lombok.*;
import java.util.UUID;
import com.inventory.backend.entities.Organization;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductResponse {

    private UUID id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String image;

    private CategoryResponse category;

    private Organization organization;
}
