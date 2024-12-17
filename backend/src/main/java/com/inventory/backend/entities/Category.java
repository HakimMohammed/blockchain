package com.inventory.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "categories")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name" , nullable = false , length = 25 )
    private String name;

    @Column(name = "description" , nullable = false , length = 50 )
    private String description;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> products;
}
