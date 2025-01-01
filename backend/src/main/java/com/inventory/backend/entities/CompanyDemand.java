package com.inventory.backend.entities;

import com.inventory.backend.enums.DemandStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "companies_demands")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDemand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "quantity", nullable = false , length = 10 )
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Organization company;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Organization supplier;

    @Column(name = "status", nullable = false , length = 10 )
    @Enumerated(EnumType.STRING)
    private DemandStatus status;
}
