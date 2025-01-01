package com.inventory.backend.services;

import com.inventory.backend.dtos.company_demands.CompanyDemandResponse;
import com.inventory.backend.entities.CompanyDemand;

public interface DemandService extends CrudService<CompanyDemand , CompanyDemandResponse> {
}
