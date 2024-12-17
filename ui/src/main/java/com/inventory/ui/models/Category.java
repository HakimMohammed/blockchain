package com.inventory.ui.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category {
    private Integer id;
    private String name;
    private String description;
    private List<Product> products;

    public Category() {}

    // insert data
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // update or retrieve data
    public Category(Integer id, String name, String description, List<Product> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products;
    }
}
