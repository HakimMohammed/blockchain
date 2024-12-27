package com.inventory.backend.services.impl;

import com.inventory.backend.dtos.category.CategoryResponse;
import com.inventory.backend.entities.Category;
import com.inventory.backend.mappers.CategoryMapper;
import com.inventory.backend.repos.CategoryRepository;
import com.inventory.backend.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category updateById(UUID id, Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryRepository.save(category);
        }
        return null;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(UUID id) {
        categoryRepository.deleteById(id);
    }
}
