package com.inventory.ui.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

public class ProductService {
    private static ProductService instance;
    private final List<Product> products = new ArrayList<>();
    private final HttpService httpService;
    private final ObjectMapper objectMapper;

    private ProductService() {
        this.httpService = new HttpService();
        this.objectMapper = new ObjectMapper();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public void createProduct(Product product) {
        try {
            Response response = httpService.post("products", product);
            if (response.isSuccessful()) {
                Product createdProduct = objectMapper.readValue(response.body().string(), Product.class);
                products.add(createdProduct);
            } else {
                System.out.println("Failed to create product: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }

    public void updateProduct(Product product) {
        try {
            Response response = httpService.put("products/" + product.getId(), product);
            if (response.isSuccessful()) {
                int index = findProductIndex(product.getId());
                if (index != -1) {
                    products.set(index, product);
                }
            } else {
                System.out.println("Failed to update product: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            Response response = httpService.delete("products/" + id);
            if (response.isSuccessful()) {
                products.removeIf(p -> p.getId().equals(id));
            } else {
                System.out.println("Failed to delete product: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public ObservableList<Product> getProducts() {
        try {
            Response response = httpService.get("products");
            if (response.isSuccessful()) {
                List<Product> productList = objectMapper.readValue(response.body().string(), new TypeReference<List<Product>>() {});
                return FXCollections.observableArrayList(productList);
            } else {
                System.out.println("Failed to retrieve products: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        return FXCollections.observableArrayList();
    }

    public int getTotalProducts() {
        return 10;
    }

    private int findProductIndex(UUID id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}