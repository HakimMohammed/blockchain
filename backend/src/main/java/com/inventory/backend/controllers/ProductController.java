package com.inventory.backend.controllers;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.dtos.product.CreateProductRequest;
import com.inventory.backend.dtos.product.UpdateProductRequest;
import com.inventory.backend.entities.Product;
import com.inventory.backend.entities.Category;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.mappers.ProductMapper;
import com.inventory.backend.services.ProductService;
import com.inventory.backend.services.CategoryService;
import com.inventory.backend.services.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<Product> products = productService.findAll();
        List<ProductResponse> responses = productMapper.toResponseList(products);  // Use the new method here
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        Product product = productService.findById(id);
        ProductResponse response = productMapper.toResponse(product);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@RequestBody @Valid CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(productMapper.toResponse(savedProduct), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateById(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {

        Product existingProduct = productService.findById(id);
        Product updatedProduct = productMapper.toEntity(request, existingProduct);
        updatedProduct = productService.save(updatedProduct);

        return ResponseEntity.ok(productMapper.toResponse(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
