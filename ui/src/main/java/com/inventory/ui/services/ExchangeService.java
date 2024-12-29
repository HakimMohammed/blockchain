package com.inventory.ui.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.dtos.exchange.TradeRequest;
import com.inventory.ui.models.Exchange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

public class ExchangeService {
    private static ExchangeService instance;
    private final HttpService httpService;
    private final List<Exchange> exchanges = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public ExchangeService() {
        this.httpService = new HttpService();
        this.objectMapper = new ObjectMapper();
    }

    public static ExchangeService getInstance() {
        if (instance == null) {
            instance = new ExchangeService();
        }
        return instance;
    }

    public ObservableList<Exchange> getExchanges(int fromIndex, int count) {
        try {
            Response response = httpService.get("exchanges");
            if (response.isSuccessful()) {
                List<Exchange> exhangeList = objectMapper.readValue(response.body().string(), new TypeReference<List<Exchange>>() {});
                return FXCollections.observableArrayList(exhangeList);
            } else {
                System.out.println("Failed to retrieve exchanges: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error retrieving exchanges: " + e.getMessage());
        }
        return FXCollections.observableArrayList();
    }

    public int getTotalExchanges() {
        return 10;
    }

    private int findExchangeIndex(String id) {
        return exchanges.indexOf(exchanges.stream().filter(product -> product.getExchange_id().equals(id)).findFirst().get());
    }

    public void tradeProducts(String senderId, String productId, int quantity) {
        try {
            TradeRequest tradeRequest = TradeRequest.builder().senderId(senderId).receiverId("company").productId(productId).quantity(quantity).build();
            Response response = httpService.post("exchanges/trade", tradeRequest);

            if (!response.isSuccessful()) {
                System.out.println("Failed to trade products: " + response.body().string());
            } else {
                System.out.println("Trade successful!");
            }
        } catch (IOException e) {
            System.out.println("Error trading products: " + e.getMessage());
        }
    }
}
