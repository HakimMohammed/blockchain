package com.inventory.backend.services;

import com.inventory.backend.dtos.product.ProductResponse;
import com.inventory.backend.entities.Product;

public interface ProductService extends CrudService<Product , ProductResponse> { }
