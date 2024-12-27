package com.inventory.backend.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inventory.backend.entities.Exchange;
import com.inventory.backend.entities.Inventory;
import com.inventory.backend.enums.TransactionType;

import java.lang.reflect.Type;
import java.util.List;

public class BlockchainMapper {

    private static final Gson gson = new Gson();

    public static List<Exchange> fromJsonToExchangeList(byte[] json) {
        Type listType = new TypeToken<List<Exchange>>() {
        }.getType();
        return gson.fromJson(new String(json), listType);
    }

    public static Inventory fromJsonToInventory(byte[] json) {
        return gson.fromJson(new String(json), Inventory.class);
    }

    public static Exchange fromJsonToExchange(byte[] json) {
        return gson.fromJson(new String(json), Exchange.class);
    }

    public static TransactionType fromJsonToTransactionType(byte[] json) {
        return gson.fromJson(new String(json), TransactionType.class);
    }
}
