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
    private final CategoryMapper categoryMapper;

    @Override
    public Product toEntity(CreateProductRequest request) {
        Optional<Category> category = Optional.ofNullable(categoryService.findById(request.getCategoryId()));
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setCategory(category.get());
        return product;
    }

    @Override
    public Product toEntity(UpdateProductRequest request, Product existingProduct) {
        if (request.getCategoryId() != null) {
            Category category = categoryService.findById(request.getCategoryId());
            existingProduct.setCategory(category);
        }
        if (request.getName() != null) existingProduct.setName(request.getName());
        if (request.getDescription() != null) existingProduct.setDescription(request.getDescription());
        if (request.getPrice() != null) existingProduct.setPrice(request.getPrice());
        if (request.getImage() != null) existingProduct.setImage(request.getImage());

        return existingProduct;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        return null;
    }



    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
