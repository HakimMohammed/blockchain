package com.inventory.backend.dtos.company_demands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.enums.DemandStatus;
import java.util.UUID;
import lombok.Data;

@Data
public class CompanyDemandResponse {
    private UUID id;
    private int quantity;
    private String productName;
    private String companyName;
    private String supplierName;
    private DemandStatus status;
}