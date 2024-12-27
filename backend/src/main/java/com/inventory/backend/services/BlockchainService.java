package com.inventory.backend.services;

import com.inventory.backend.entities.Exchange;
import com.inventory.backend.entities.Inventory;
import com.inventory.backend.enums.TransactionType;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.CommitException;

import java.util.List;

public interface BlockchainService {
    String initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException;
    List<Exchange> getExchangesByOrganization(String organization) throws GatewayException;
    Inventory getInventory(String inventoryId) throws GatewayException;
    Exchange getExchange(String exchangeId) throws GatewayException;
    String createInventory(String inventoryId, String organization) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String createExchange(String exchangeId, String productId, String organization, int quantity, String date, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String updateInventoryStock(String inventoryId, String productId, int quantity, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String trade(String sender, String receiver, String productId, int quantity) throws EndorseException, SubmitException, CommitStatusException, CommitException;
}
