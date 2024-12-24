package com.inventory.backend.metier;

import com.inventory.backend.enums.TransactionType;
import org.hyperledger.fabric.client.*;

import java.util.Map;

public interface BlockchainMetier {
    public String initLedger(Contract contract) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    public byte[] getExchangesByOrganization(Contract contract, String organization) throws GatewayException;
    public byte[] getInventory(Contract contract, String inventory) throws GatewayException;
    public byte[] getExchange(Contract contract, String exchange) throws GatewayException;
    public String createInventory(Contract contract, String inventory, String organization, Map<String, Integer> stock) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    public String createExchange(Contract contract, String exchange_id, String product_id, String organization, int quantity, String date, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    public String updateInventoryStock(Contract contract, String inventory, String product_id, int quantity, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException;
    public String trade(Contract contract, String sender, String receiver, String product_id, int quantity) throws EndorseException, SubmitException, CommitStatusException, CommitException;
}
