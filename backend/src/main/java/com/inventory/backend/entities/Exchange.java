package com.inventory.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inventory.backend.enums.TransactionType;

import java.util.UUID;

public record Exchange(String exchange_id, UUID product_id, String organization, int quantity, String date,
                       TransactionType transaction) {
    public Exchange(@JsonProperty("exchange_id") final String exchange_id,
                    @JsonProperty("product_id") final UUID product_id,
                    @JsonProperty("organization") final String organization,
                    @JsonProperty("quantity") final int quantity,
                    @JsonProperty("date") final String date,
                    @JsonProperty("transaction") final TransactionType transaction) {
        this.exchange_id = exchange_id;
        this.product_id = product_id;
        this.organization = organization;
        this.quantity = quantity;
        this.date = date;
        this.transaction = transaction;
    }
}