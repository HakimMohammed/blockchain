package com.inventory.ui.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private Integer category_id;

    private Integer userId;

    private Category category;

    private User user;

    // Default constructor (required for reflection)
    public Product() {
    }

    public Product(String name, String description, Double price, Integer quantity, Integer category_id, Integer userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category_id = category_id;
        this.userId = userId;
    }

    public Product(Integer id, String name, String description, Double price, Integer quantity, Integer category_id, Integer userId, Category category, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category_id = category_id;
        this.userId = userId;
        this.category = category;
        this.user = user;
    }
}
