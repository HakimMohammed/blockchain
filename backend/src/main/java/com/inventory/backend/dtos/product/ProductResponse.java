package com.inventory.backend.dtos.product;

import lombok.*;

import java.util.UUID;

import com.inventory.backend.entities.Organization;

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

    private Organization organization;
}
