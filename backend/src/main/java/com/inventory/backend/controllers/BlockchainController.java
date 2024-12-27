package com.inventory.backend.controllers;

import com.inventory.backend.entities.Exchange;
import com.inventory.backend.entities.Inventory;
import com.inventory.backend.enums.TransactionType;
import com.inventory.backend.services.BlockchainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blockchain")
@AllArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

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
    public ResponseEntity<List<Exchange>> getExchangesByOrganization(@RequestParam String organization) {
        try {
            List<Exchange> exchanges = blockchainService.getExchangesByOrganization(organization);
            return ResponseEntity.ok(exchanges);  // Return List of Exchange objects directly
        } catch (Exception e) {
e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/inventory")
    public ResponseEntity<Inventory> getInventory(@RequestParam String inventory) {
        try {
            Inventory inventoryData = blockchainService.getInventory(inventory);
            return ResponseEntity.ok(inventoryData);  // Return Inventory object directly
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<Exchange> getExchange(@PathVariable String exchange) {
        try {
            Exchange exchangeData = blockchainService.getExchange(exchange);
            return ResponseEntity.ok(exchangeData);  // Return Exchange object directly
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
