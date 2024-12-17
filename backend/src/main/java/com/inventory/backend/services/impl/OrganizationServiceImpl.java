package com.inventory.backend.services.impl;

import com.inventory.backend.entities.Organization;
import com.inventory.backend.repos.OrganizationRepository;
import com.inventory.backend.services.OrganizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization findById(UUID id) {
        return organizationRepository.findById(id).orElse(null);
    }

    @Override
    public Organization updateById(UUID id, Organization organization) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            return organizationRepository.save(organization);
        }
        return null;
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(UUID id) {
        organizationRepository.deleteById(id);
    }
}
