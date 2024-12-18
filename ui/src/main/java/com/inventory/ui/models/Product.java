package com.inventory.ui.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private UUID id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String image;

    private Category category;

    private Organization organization;

}
