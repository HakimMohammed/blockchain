package com.inventory.backend.controllers;

import com.inventory.backend.dtos.exchange.TradeRequest;
import com.inventory.backend.entities.Exchange;
import com.inventory.backend.services.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/exchanges")
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping
    public ResponseEntity<List<Exchange>> getExchangesByOrganization() {
        try {
            List<Exchange> exchanges = exchangeService.findAll();
            return ResponseEntity.ok(exchanges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/trade")
    public ResponseEntity<String> tradeProducts(@RequestBody TradeRequest tradeRequest) {
        try {
            exchangeService.tradeProducts(tradeRequest.getSenderId(), tradeRequest.getReceiverId(), tradeRequest.getProductId(), tradeRequest.getQuantity());
            return ResponseEntity.ok("Trade executed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing trade: " + e.getMessage());
        }
    }

}
