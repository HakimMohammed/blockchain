package com.inventory.backend.entities;

import com.inventory.backend.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "organizations")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name" , nullable = false , length = 25 )
    private String name;

    @Column(name = "email" , nullable = false , length = 50 , unique = true )
    private String email;

    @Column(name = "phone" , nullable = false , length = 15 )
    private String phone;

    @Column(name = "address" , nullable = false , length = 50 )
    private String address;

    @Column(name = "city" , nullable = false , length = 25 )
    private String city;

    @Column(name = "state" , nullable = false , length = 25 )
    private String state;

    @Column(name = "country" , nullable = false , length = 25 )
    private String country;

    @Column(name = "zip" , nullable = false , length = 10 )
    private String zip;

    @Column(name = "description" , nullable = false , length = 50 )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type" , nullable = false , length = 20 )
    private OrganizationType type;
}
