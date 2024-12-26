package com.inventory.backend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.inventory.backend.enums.TransactionType;
import com.inventory.backend.services.BlockchainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/blockchain")
@AllArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    @PostMapping("/initLedger")
    public ResponseEntity<Void> initLedger() {
        try {
            blockchainService.initLedger();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exchanges")
    public ResponseEntity<String> getExchangesByOrganization(@RequestParam String organization) {
        try {
            return ResponseEntity.ok(prettyJson(blockchainService.getExchangesByOrganization(organization)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/inventory")
    public ResponseEntity<String> getInventory(@RequestParam String inventory) {
        try {
            return ResponseEntity.ok(prettyJson(blockchainService.getInventory(inventory)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<String> getExchange(@PathVariable String exchange) {
        try {
            return ResponseEntity.ok(prettyJson(blockchainService.getExchange(exchange)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/inventory")
    public ResponseEntity<Void> createInventory(@RequestParam String inventory, @RequestParam String organization) {
        try {
            blockchainService.createInventory(inventory, organization);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/exchange")
    public ResponseEntity<Void> createExchange(
            @RequestParam String exchange_id, @RequestParam String product_id, @RequestParam String organization,
            @RequestParam int quantity, @RequestParam String date, @RequestParam TransactionType transaction) {
        try {
            blockchainService.createExchange(exchange_id, product_id, organization, quantity, date, transaction);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/inventory")
    public ResponseEntity<Void> updateInventoryStock(
            @RequestParam String inventory, @RequestParam String product_id, @RequestParam int quantity,
            @RequestParam TransactionType transaction) {
        try {
            blockchainService.updateInventoryStock(inventory, product_id, quantity, transaction);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trade")
    public ResponseEntity<Void> trade(
            @RequestParam String sender, @RequestParam String receiver, @RequestParam String product_id,
            @RequestParam int quantity) {
        try {
            blockchainService.trade(sender, receiver, product_id, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}