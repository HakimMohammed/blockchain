package com.inventory.backend.metier;

import com.google.gson.Gson;
import com.inventory.backend.enums.TransactionType;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class BlockchainMetierImpl implements BlockchainMetier {

    public BlockchainMetierImpl() {}

    @Override
    public String initLedger(Contract contract) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of inventories and transactions on the ledger");

        contract.submitTransaction("InitLedger");

        return "Transaction committed successfully";
    }

    @Override
    public byte[] getExchangesByOrganization(Contract contract, String organization) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getExchangesByOrganization, function returns all the exchanges for a specific organization");

        return contract.evaluateTransaction("getExchangesByOrganization", organization);
    }

    @Override
    public byte[] getInventory(Contract contract, String inventory) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getInventory, function returns the inventory for a specific organization");

        return contract.evaluateTransaction("getInventory", inventory);
    }

    @Override
    public byte[] getExchange(Contract contract, String exchange) throws GatewayException {
        System.out.println("\n--> Evaluate Transaction: getExchange, function returns the exchange for a specific exchange id");

        return contract.evaluateTransaction("getExchange", exchange);
    }

    @Override
    public String createInventory(Contract contract, String inventory, String organization, Map<String, Integer> stock) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: CreateInventory, function creates a new inventory on the ledger");

        contract.submitTransaction("CreateInventory", inventory, organization, new Gson().toJson(stock));

        return "Transaction committed successfully";
    }

    @Override
    public String createExchange(Contract contract, String exchange_id, String product_id, String organization, int quantity, String date, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: CreateExchange, function creates a new exchange on the ledger");

        String qte = Integer.toString(quantity);
        contract.submitTransaction("CreateExchange", exchange_id, product_id, organization, qte, date, transaction.toString());

        return "Transaction committed successfully";
    }

    @Override
    public String updateInventoryStock(Contract contract, String inventory, String product_id, int quantity, TransactionType transaction) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: UpdateInventoryStock, function updates the stock of a product in an inventory");

        String qte = Integer.toString(quantity);
        contract.submitTransaction("UpdateInventoryStock", inventory, product_id, qte, transaction.toString());

        return "Transaction committed successfully";
    }

    @Override
    public String trade(Contract contract, String sender, String receiver, String product_id, int quantity) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: Trade, function trades a product between two organizations");

        String qte = Integer.toString(quantity);
        contract.submitTransaction("Trade", sender, receiver, product_id, qte);

        return "Transaction committed successfully";
    }
}
