package com.inventory.backend.services.impl;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.entities.Inventory;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.mappers.CategoryMapper;
import com.inventory.backend.repos.ProductRepository;
import com.inventory.backend.services.AuthenticationService;
import com.inventory.backend.services.BlockchainService;
import com.inventory.backend.services.ProductService;
import jakarta.transaction.Transactional;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BlockchainService blockchainService;
    private final CategoryMapper categoryMapper;
    private final AuthenticationService authenticationService;

    public ProductServiceImpl(ProductRepository productRepository,
                              BlockchainService blockchainService,
                              AuthenticationService authenticationService,
                              CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.blockchainService = blockchainService;
        this.authenticationService = authenticationService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<ProductResponse> findAll() {
        try {
            Organization userOrganization = authenticationService.getAuthenticatedUser().getOrganization();
            System.out.println("User organization name: " + userOrganization.getName());
            Inventory inventory = blockchainService.getInventory(userOrganization.getName());
            List<Product> products = productRepository.findAllByIdIn(inventory.stock().keySet());
            return products.stream()
                    .map(product -> ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .image(product.getImage())
                            .quantity(inventory.stock().get(product.getId()))
                            .category(categoryMapper.toResponse(product.getCategory()))
                            .organization(userOrganization)
                            .build())
                    .toList();

        } catch (GatewayException e) {
            throw new RuntimeException("Error retrieving inventory from blockchain: " + e.getMessage(), e);
        }
    }

    @Override
    public Product findById(UUID id) {
        try {
            String userOrganizationName = authenticationService.getAuthenticatedUser().getOrganization().getName();
            Inventory inventory = blockchainService.getInventory(userOrganizationName);
            if (inventory.stock().containsKey(id)) {
                return productRepository.findById(id).orElse(null);
            }
            return null;
        } catch (GatewayException e) {
            throw new RuntimeException("Error retrieving inventory from blockchain: " + e.getMessage(), e);
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
