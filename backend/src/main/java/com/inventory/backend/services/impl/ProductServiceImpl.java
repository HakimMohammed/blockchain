package com.inventory.backend.services.impl;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.dtos.user.UserResponse;
import com.inventory.backend.entities.*;
import com.inventory.backend.mappers.CategoryMapper;
import com.inventory.backend.repos.ProductRepository;
import com.inventory.backend.repos.UserRepository;
import com.inventory.backend.services.AuthenticationService;
import com.inventory.backend.services.BlockchainService;
import com.inventory.backend.services.OrganizationService;
import com.inventory.backend.services.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BlockchainService blockchainService;
    private final CategoryMapper categoryMapper;
    private final String userOrganizationName;

    public ProductServiceImpl(ProductRepository productRepository, BlockchainService blockchainService, AuthenticationService authenticationService, CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.blockchainService = blockchainService;
        this.categoryMapper = categoryMapper;
        // check if the user is authenticated
        this.userOrganizationName = authenticationService.getAuthenticatedUser() != null ? authenticationService.getAuthenticatedUser().getOrganization().getName() : "company";
        System.out.println("userOrganizationName: " + userOrganizationName);
    }

    @Override
    public List<ProductResponse> findAll() {
        try {
            Inventory inventory = blockchainService.getInventory(userOrganizationName);
            List<Product> products = productRepository.findAllByIdIn(inventory.getStock().keySet());
            return products.stream()
                    .map(product -> ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .image(product.getImage())
                            .quantity(inventory.getStock().get(product.getId()))
                            .category(categoryMapper.toResponse(product.getCategory()))
                            .organization(product.getOrganization())
                            .build())
                    .toList();

        } catch (GatewayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(UUID id) {
        try {
            Inventory inventory = blockchainService.getInventory(userOrganizationName);
            if (inventory.getStock().containsKey(id)) {
                return productRepository.findById(id).orElse(null);
            }
            return null;
        } catch (GatewayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product updateById(UUID id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }
}
