package com.inventory.backend.dtos.category;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponse {

    private UUID id;
    private String name;
    private String description;
}