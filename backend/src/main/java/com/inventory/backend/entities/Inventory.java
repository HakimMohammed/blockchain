package com.inventory.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public record Inventory(String inventory_id, String organization, Map<UUID, Integer> stock) {
    public Inventory(@JsonProperty("inventory_id") final String inventory_id,
                     @JsonProperty("organization") final String organization,
                     @JsonProperty("stock") final Map<UUID, Integer> stock) {
        this.inventory_id = inventory_id;
        this.organization = organization;
        this.stock = new TreeMap<>(stock); // Ensures the stock is always sorted
    }
}
