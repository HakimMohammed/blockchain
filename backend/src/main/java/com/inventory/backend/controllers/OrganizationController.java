package com.inventory.backend.controllers;

import com.inventory.backend.dtos.organization.CreateOrganizationRequest;
import com.inventory.backend.dtos.organization.OrganizationResponse;
import com.inventory.backend.dtos.organization.UpdateOrganizationRequest;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.mappers.OrganizationMapper;
import com.inventory.backend.services.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/organizations")
@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAll() {
        List<Organization> organizations = organizationService.findAll();
        List<OrganizationResponse> responses = organizations.stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable UUID id) {
        System.out.println(id);
        Optional<Organization> organization = Optional.ofNullable(organizationService.findById(id));
        OrganizationResponse response = organizationMapper.toResponse(organization.orElse(null));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrganizationResponse> save(@RequestBody @Valid CreateOrganizationRequest request) {
        Organization organization = organizationMapper.toEntity(request);
        Organization savedOrganization = organizationService.save(organization);
        return new ResponseEntity<>(organizationMapper.toResponse(savedOrganization), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateById(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateOrganizationRequest request) {

        Organization existingOrganization = organizationService.findById(id);
        Organization updatedOrganization = organizationMapper.toEntity(request, existingOrganization);
        updatedOrganization = organizationService.save(updatedOrganization);
        return ResponseEntity.ok(organizationMapper.toResponse(updatedOrganization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
