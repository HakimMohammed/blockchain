package com.inventory.ui.dtos.company_demand;

import com.inventory.ui.enums.DemandStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyDemandRequest {
    private UUID id;
    private int quantity;
    private UUID productId;
    private DemandStatus status;
}
