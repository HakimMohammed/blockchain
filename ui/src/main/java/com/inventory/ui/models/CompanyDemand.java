package com.inventory.ui.models;

import com.inventory.ui.enums.DemandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDemand {
    private UUID id;
    private int quantity;
    private Product product;
    private Organization company;
    private Organization supplier;
    private DemandStatus status;
}