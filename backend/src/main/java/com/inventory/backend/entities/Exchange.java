package com.inventory.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inventory.backend.enums.TransactionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public final class Exchange {
    private final String exchange_id;
    private final UUID product_id;
    private final String organization;
    private final int quantity;
    private final String date;
    private final TransactionType transaction;

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

