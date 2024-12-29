package com.inventory.backend.services;

import com.inventory.backend.entities.Exchange;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeService {
    private final BlockchainService blockchainService;
    private final AuthenticationService authenticationService;

    public ExchangeService(BlockchainService blockchainService, AuthenticationService authenticationService) {
        this.blockchainService = blockchainService;
        this.authenticationService = authenticationService;

    }

    public List<Exchange> findAll() {
        String userOrganizationName = authenticationService.getAuthenticatedUser().getOrganization().getName();
        try {
            return blockchainService.getExchangesByOrganization(userOrganizationName);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving exchanges from blockchain: " + e.getMessage(), e);
        }
    }
}
