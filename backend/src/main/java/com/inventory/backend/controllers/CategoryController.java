package com.inventory.backend.controllers;

import com.inventory.backend.dtos.category.CategoryResponse;
import com.inventory.backend.dtos.category.CreateCategoryRequest;
import com.inventory.backend.dtos.category.UpdateCategoryRequest;
import com.inventory.backend.entities.Category;
import com.inventory.backend.mappers.CategoryMapper;
import com.inventory.backend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> responses = categoryMapper.toResponseList(categories);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryService.save(category);
        return new ResponseEntity<>(categoryMapper.toResponse(savedCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id, @RequestBody @Valid UpdateCategoryRequest request) {
        Category existingCategory = categoryService.findById(id);
        Category updatedCategory = categoryMapper.toEntity(request, existingCategory);
        categoryService.save(updatedCategory);
        return ResponseEntity.ok(categoryMapper.toResponse(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
