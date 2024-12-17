package com.inventory.backend.mappers;

import com.inventory.backend.dtos.category.CategoryResponse;
import com.inventory.backend.dtos.category.CreateCategoryRequest;
import com.inventory.backend.dtos.category.UpdateCategoryRequest;
import com.inventory.backend.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryMapper implements Mapper<Category , CreateCategoryRequest , UpdateCategoryRequest , CategoryResponse>{

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }

    @Override
    public Category toEntity(UpdateCategoryRequest request, Category existingCategory) {
        if (request.getName() != null) existingCategory.setName(request.getName());
        if (request.getDescription() != null) existingCategory.setDescription(request.getDescription());
        return existingCategory;
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    @Override
    public List<CategoryResponse> toResponseList(List<Category> products) {
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
