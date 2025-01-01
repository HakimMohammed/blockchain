package com.inventory.backend.controllers;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.dtos.product.CreateProductRequest;
import com.inventory.backend.entities.Product;
import com.inventory.backend.mappers.ProductMapper;
import com.inventory.backend.services.ProductService;
import com.inventory.backend.sockets.WebSocketHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<ProductResponse> responses = productService.findAll();
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

}
