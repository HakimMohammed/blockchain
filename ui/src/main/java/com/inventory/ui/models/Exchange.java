package com.inventory.ui.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inventory.ui.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Exchange {
    @JsonProperty("exchange_id")
    private final String exchange_id;
    @JsonProperty("product_id")
    private final UUID product_id;
    @JsonProperty("organization")
    private final String organization;
    @JsonProperty("quantity")
    private final int quantity;
    @JsonProperty("date")
    private final String date;
    @JsonProperty("transaction")
    private final TransactionType transaction;

    @JsonCreator
    public Exchange(
            @JsonProperty("exchange_id") String exchange_id,
            @JsonProperty("product_id") UUID product_id,
            @JsonProperty("organization") String organization,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("date") String date,
            @JsonProperty("transaction") TransactionType transaction) {
        this.exchange_id = exchange_id;
        this.product_id = product_id;
        this.organization = organization;
        this.quantity = quantity;
        this.date = date;
        this.transaction = transaction;
    }
}