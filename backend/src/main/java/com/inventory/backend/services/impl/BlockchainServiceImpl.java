package com.inventory.backend.services.impl;

import com.inventory.backend.entities.Exchange;
import com.inventory.backend.entities.Inventory;
import com.inventory.backend.enums.TransactionType;
import com.inventory.backend.mappers.BlockchainMapper;
import com.inventory.backend.services.BlockchainService;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "stock");

    private final Contract contract;

    public BlockchainServiceImpl(Gateway gateway) {
        try {
            Network network = gateway.getNetwork(CHANNEL_NAME);
            this.contract = network.getContract(CHAINCODE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize BlockchainController", e);
        }
    }

    @Override
    public String initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: initLedger, function creates the initial set of inventories and transactions on the ledger");

        contract.submitTransaction("initLedger");

        return "Transaction committed successfully";
    }

    @Override
    public List<Exchange> getExchangesByOrganization(String organization) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getExchangesByOrganization, function returns all the exchanges for a specific organization");

        byte[] result = contract.evaluateTransaction("getExchangesByOrganization", organization);
        return BlockchainMapper.fromJsonToExchangeList(result);
    }

    @Override
    public Inventory getInventory(String inventoryId) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getInventory, function returns the inventory for a specific organization");

        byte[] result = contract.evaluateTransaction("getInventory", inventoryId);
        return BlockchainMapper.fromJsonToInventory(result);
    }

    @Override
    public Exchange getExchange(String exchangeId) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getExchange, function returns the exchange for a specific exchange id");

        byte[] result = contract.evaluateTransaction("getExchange", exchangeId);
        return BlockchainMapper.fromJsonToExchange(result);
    }

    @Override
    public String createInventory(String inventoryId, String organization) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: CreateInventory, function creates a new inventory on the ledger");

        contract.submitTransaction("createInventory", inventoryId, organization);

        return "Transaction committed successfully";
    }

    @Override
    public String createExchange(String exchangeId, String productId, String organization, int quantity, String date, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: CreateExchange, function creates a new exchange on the ledger");

        String quantityStr = Integer.toString(quantity);

        var commit = contract.newProposal("createExchange")
                .addArguments(exchangeId, productId, organization, quantityStr, date, transaction.toString())
                .build()
                .endorse()
                .submitAsync();

        var result = commit.getResult();
        String exchangeResult = new String(result, StandardCharsets.UTF_8);

        System.out.println("*** Transaction result: " + exchangeResult);

        var status = commit.getStatus();
        if (!status.isSuccessful()) {
            throw new RuntimeException("Transaction " + status.getTransactionId() +
                    " failed to commit with status code " + status.getCode());
        }

        return exchangeResult;
    }

    @Override
    public String updateInventoryStock(String inventoryId, String productId, int quantity, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: UpdateInventoryStock, function updates the stock of a product in an inventory");

        String quantityStr = Integer.toString(quantity);
        contract.submitTransaction("UpdateInventoryStock", inventoryId, productId, quantityStr, transaction.toString());

        return "Transaction committed successfully";
    }

    @Override
    public String trade(String sender, String receiver, String productId, int quantity) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: Trade, function trades a product between two organizations");

        String quantityStr = Integer.toString(quantity);
        var commit = contract.newProposal("trade")
                .addArguments(sender, receiver, productId, quantityStr)
                .build()
                .endorse()
                .submitAsync();

        var result = commit.getResult();
        String tradeResult = new String(result, StandardCharsets.UTF_8);

        System.out.println("*** Transaction result: " + tradeResult);

        var status = commit.getStatus();
        if (!status.isSuccessful()) {
            throw new RuntimeException("Transaction " + status.getTransactionId() +
                    " failed to commit with status code " + status.getCode());
        }

        return tradeResult;
    }
}
