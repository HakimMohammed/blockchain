package com.inventory.backend.dtos.company_demands;

import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.enums.DemandStatus;
import java.util.UUID;
import lombok.Data;

@Data
public class CompanyDemandResponse {
    private UUID id;
    private int quantity;
    private Product product;
    private Organization company;
    private Organization supplier;
    private DemandStatus status;
}