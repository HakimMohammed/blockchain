package com.inventory.backend.dtos.company_demands;

import com.inventory.backend.enums.DemandStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CompanyDemandUpdate {
    @Min(1)
    private int quantity;

    @NotNull
    private UUID productId;

    @NotNull
    private DemandStatus status;
}