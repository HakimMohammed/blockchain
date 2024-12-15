package com.inventory.backend.mappers;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.dtos.product.CreateProductRequest;
import com.inventory.backend.dtos.product.UpdateProductRequest;
import com.inventory.backend.entities.Product;
import com.inventory.backend.entities.Category;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.services.CategoryService;
import com.inventory.backend.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapper implements Mapper<Product, CreateProductRequest, UpdateProductRequest, ProductResponse> {

    private final CategoryService categoryService;
    private final OrganizationService organizationService;


    public Product toEntity(CreateProductRequest request) {
        Optional<Organization> organization = Optional.ofNullable(organizationService.findById(request.getOrganizationId()));
        Optional<Category> category = Optional.ofNullable(categoryService.findById(request.getCategoryId()));
        if (organization.isEmpty() || category.isEmpty()) return null;
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImage(request.getImage());
        product.setCategory(category.get());
        product.setOrganization(organization.get());
        return product;
    }


    public Product toEntity(UpdateProductRequest request, Product existingProduct) {
        if (request.getCategoryId() != null) {
            Category category = categoryService.findById(request.getCategoryId());
            existingProduct.setCategory(category);
        }
        if (request.getOrganizationId() != null) {
            Organization organization = organizationService.findById(request.getOrganizationId());
            existingProduct.setOrganization(organization);
        }
        if (request.getName() != null) existingProduct.setName(request.getName());
        if (request.getDescription() != null) existingProduct.setDescription(request.getDescription());
        if (request.getPrice() != null) existingProduct.setPrice(request.getPrice());
        if (request.getQuantity() != null) existingProduct.setQuantity(request.getQuantity());
        if (request.getImage() != null) existingProduct.setImage(request.getImage());

        return existingProduct;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getImage(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getOrganization() != null ? product.getOrganization().getId() : null,
                product.getOrganization() != null ? product.getOrganization().getName() : null
        );
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
