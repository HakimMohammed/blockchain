package com.inventory.ui.models;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Category {
    private UUID id;

    private String name;

    private String description;

    private List<Product> products;
}
