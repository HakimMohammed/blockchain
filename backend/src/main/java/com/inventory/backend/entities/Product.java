package com.inventory.backend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "products")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name" , nullable = false , length = 25 )
    private String name;

    @Column(name = "description" , nullable = false , length = 50 )
    private String description;

    @Column(name = "price" , nullable = false )
    private double price;

    @Column(name = "image" )
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
