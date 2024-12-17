package com.inventory.backend.dtos.product;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductResponse {

    private UUID id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String image;

    private UUID categoryId;

    private String categoryName;

    private UUID organizationId;

    private String organizationName;
}
