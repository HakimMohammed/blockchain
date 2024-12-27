package com.inventory.backend.services;

import com.inventory.backend.dtos.category.CategoryResponse;
import com.inventory.backend.entities.Category;

public interface CategoryService extends CrudService<Category , CategoryResponse> {
}
