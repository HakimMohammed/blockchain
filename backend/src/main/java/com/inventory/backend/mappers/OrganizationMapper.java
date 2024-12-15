package com.inventory.backend.mappers;

import com.inventory.backend.dtos.organization.CreateOrganizationRequest;
import com.inventory.backend.dtos.organization.UpdateOrganizationRequest;
import com.inventory.backend.dtos.organization.OrganizationResponse;
import com.inventory.backend.entities.Organization;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationMapper implements Mapper<Organization, CreateOrganizationRequest, UpdateOrganizationRequest, OrganizationResponse> {

    @Override
    public Organization toEntity(CreateOrganizationRequest request) {
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setAddress(request.getAddress());
        organization.setCity(request.getCity());
        organization.setState(request.getState());
        organization.setCountry(request.getCountry());
        organization.setZip(request.getZip());
        organization.setDescription(request.getDescription());
        organization.setType(request.getType());
        return organization;
    }

    @Override
    public Organization toEntity(UpdateOrganizationRequest request, Organization existingOrganization) {
        if (request.getName() != null) existingOrganization.setName(request.getName());
        if (request.getEmail() != null) existingOrganization.setEmail(request.getEmail());
        if (request.getPhone() != null) existingOrganization.setPhone(request.getPhone());
        if (request.getAddress() != null) existingOrganization.setAddress(request.getAddress());
        if (request.getCity() != null) existingOrganization.setCity(request.getCity());
        if (request.getState() != null) existingOrganization.setState(request.getState());
        if (request.getCountry() != null) existingOrganization.setCountry(request.getCountry());
        if (request.getZip() != null) existingOrganization.setZip(request.getZip());
        if (request.getDescription() != null) existingOrganization.setDescription(request.getDescription());
        if (request.getType() != null) existingOrganization.setType(request.getType());
        return existingOrganization;
    }

    @Override
    public OrganizationResponse toResponse(Organization organization) {
        if (organization == null) return null;
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getEmail(),
                organization.getPhone(),
                organization.getAddress(),
                organization.getCity(),
                organization.getState(),
                organization.getCountry(),
                organization.getZip(),
                organization.getDescription(),
                organization.getType()
        );
    }

    @Override
    public List<OrganizationResponse> toResponseList(List<Organization> organizations) {
        return organizations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
