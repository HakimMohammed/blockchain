package com.inventory.backend.mappers;

import com.inventory.backend.dtos.company_demands.CompanyDemandCreate;
import com.inventory.backend.dtos.company_demands.CompanyDemandResponse;
import com.inventory.backend.dtos.company_demands.CompanyDemandUpdate;
import com.inventory.backend.entities.CompanyDemand;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyDemandMapper implements Mapper<CompanyDemand, CompanyDemandCreate, CompanyDemandUpdate, CompanyDemandResponse> {

    @Override
    public CompanyDemand toEntity(CompanyDemandCreate createDto) {
        CompanyDemand companyDemand = new CompanyDemand();
        companyDemand.setQuantity(createDto.getQuantity());
        companyDemand.setStatus(createDto.getStatus());
        return companyDemand;
    }

    public CompanyDemand toEntity(CompanyDemandCreate createDto, Product product, Organization company, Organization supplier) {
        CompanyDemand companyDemand = new CompanyDemand();
        companyDemand.setQuantity(createDto.getQuantity());
        companyDemand.setProduct(product);
        companyDemand.setCompany(company);
        companyDemand.setSupplier(supplier);
        companyDemand.setStatus(createDto.getStatus());
        return companyDemand;
    }

    @Override
    public CompanyDemand toEntity(CompanyDemandUpdate updateDto, CompanyDemand existingEntity) {
        throw new UnsupportedOperationException("This method requires additional parameters: Product, Organization, Supplier");
    }

    public CompanyDemand toEntity(CompanyDemandCreate createDto, Product product) {
        CompanyDemand companyDemand = new CompanyDemand();
        companyDemand.setQuantity(createDto.getQuantity());
        companyDemand.setProduct(product);
        companyDemand.setStatus(createDto.getStatus());
        return companyDemand;
    }

    public CompanyDemand toEntity(CompanyDemandUpdate updateDto, Product product, Organization company, Organization supplier) {
        CompanyDemand companyDemand = new CompanyDemand();
        companyDemand.setQuantity(updateDto.getQuantity());
        companyDemand.setProduct(product);
        companyDemand.setCompany(company);
        companyDemand.setSupplier(supplier);
        companyDemand.setStatus(updateDto.getStatus());
        return companyDemand;
    }

    @Override
    public CompanyDemandResponse toResponse(CompanyDemand companyDemand) {
        CompanyDemandResponse response = new CompanyDemandResponse();
        response.setId(companyDemand.getId());
        response.setQuantity(companyDemand.getQuantity());
        response.setProduct(companyDemand.getProduct());
        response.setCompany(companyDemand.getCompany());
        response.setSupplier(companyDemand.getSupplier());
        response.setStatus(companyDemand.getStatus());
        return response;
    }

    @Override
    public List<CompanyDemandResponse> toResponseList(List<CompanyDemand> companyDemands) {
        return companyDemands.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}