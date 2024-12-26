package com.inventory.backend.services;

import com.inventory.backend.enums.TransactionType;
import org.hyperledger.fabric.client.*;

public interface BlockchainService {
    String initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException;
    byte[] getExchangesByOrganization(String organization) throws GatewayException;
    byte[] getInventory(String inventory) throws GatewayException;
    byte[] getExchange(String exchange) throws GatewayException;
    String createInventory(String inventory, String organization) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String createExchange(String exchange_id, String product_id, String organization, int quantity, String date, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String updateInventoryStock(String inventory, String product_id, int quantity, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    String trade(String sender, String receiver, String product_id, int quantity) throws EndorseException, SubmitException, CommitStatusException, CommitException;

}
