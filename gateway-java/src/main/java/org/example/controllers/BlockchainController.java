package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.example.blockchain.GatewaySingleton;
import org.example.enums.TransactionType;
import org.example.metier.BlockchainMetierImpl;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BlockchainController {
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "stock");

    private final Contract contract;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final BlockchainMetierImpl metier;

    public BlockchainController(BlockchainMetierImpl metier) {
        this.metier = metier;
        try {
            Gateway gateway = GatewaySingleton.getInstance();
            var network = gateway.getNetwork(CHANNEL_NAME);
            this.contract = network.getContract(CHAINCODE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize BlockchainController", e);
        }
    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    public void initLedger() {
        try {
            metier.initLedger(contract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getExchangesByOrganization(String organization) {
        try {
            return prettyJson(metier.getExchangesByOrganization(contract, organization));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getInventory(String inventory) {
        try {
            return prettyJson(metier.getInventory(contract, inventory));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getExchange(String exchange) {
        try {
            return prettyJson(metier.getExchange(contract, exchange));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createInventory(String inventory, String organization) {
        try {
            metier.createInventory(contract, inventory, organization);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createExchange(String exchange_id, String product_id, String organization, int quantity, String date, TransactionType transaction) {
        try {
            metier.createExchange(contract, exchange_id, product_id, organization, quantity, date, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInventoryStock(String inventory, String product_id, int quantity, TransactionType transaction) {
        try {
            metier.updateInventoryStock(contract, inventory, product_id, quantity, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trade(String sender, String receiver, String product_id, int quantity) {
        try {
            metier.trade(contract, sender, receiver, product_id, quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            GatewaySingleton.getInstance().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
