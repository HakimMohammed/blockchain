package com.inventory.backend.services.impl;

import com.inventory.backend.dtos.company_demands.CompanyDemandCreate;
import com.inventory.backend.dtos.company_demands.CompanyDemandResponse;
import com.inventory.backend.dtos.company_demands.CompanyDemandUpdate;
import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.entities.CompanyDemand;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.entities.User;
import com.inventory.backend.mappers.CompanyDemandMapper;
import com.inventory.backend.repos.CompanyDemandRepository;
import com.inventory.backend.repos.OrganizationRepository;
import com.inventory.backend.repos.ProductRepository;
import com.inventory.backend.services.AuthenticationService;
import com.inventory.backend.services.DemandService;
import com.inventory.backend.services.OrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyDemandServiceImpl implements DemandService {


    private final CompanyDemandRepository companyDemandRepository;
    private final CompanyDemandMapper companyDemandMapper;
    private final AuthenticationService authenticationService;
    private final OrganizationService organizationService;


    @Override
    public List<CompanyDemandResponse> findAll() {
        User user = authenticationService.getAuthenticatedUser();
        return companyDemandRepository.findAllBySupplierIdAndStatusPending(user.getOrganization().getId())
                .stream()
                .map(companyDemandMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDemand findById(UUID id) {
        return companyDemandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company Demand not found"));
    }

    @Override
    public CompanyDemand updateById(UUID id, CompanyDemand companyDemand) {
        if (companyDemandRepository.existsById(id)) {
            User user = authenticationService.getAuthenticatedUser();
            companyDemand.setCompany(user.getOrganization());
            return companyDemandRepository.save(companyDemand);
        }
        return null;
    }

    @Override
    public CompanyDemand save(CompanyDemand companyDemand) {
        User user = authenticationService.getAuthenticatedUser();
        companyDemand.setCompany(user.getOrganization());
        companyDemand.setSupplier(organizationService.findById(UUID.fromString("c23cc77c-c84b-4d39-a364-35e3409b832f")));
        return companyDemandRepository.save(companyDemand);
    }

    @Override
    public void deleteById(UUID id) {
        companyDemandRepository.deleteById(id);
    }
}