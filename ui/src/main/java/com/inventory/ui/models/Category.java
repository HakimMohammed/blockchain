package com.inventory.ui.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Category {
    private UUID id;
    private String name;
    private String description;
}
