package com.inventory.ui.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image;
    private Category category;
    private Organization organization;

    // constructor for inserting new product
    public Product(String name, String description, double price, int quantity, String image, Category category, Organization organization) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.category = category;
        this.organization = organization;
    }

    // constructor for updating product
    public Product(UUID id, String name, String description, double price, int quantity, String image, Category category, Organization organization) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.category = category;
        this.organization = organization;
    }
}
