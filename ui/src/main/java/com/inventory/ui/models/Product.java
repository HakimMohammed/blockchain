package com.inventory.ui.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private UUID id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String image;

    private Category category;

    private Organization organization;

    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }
}