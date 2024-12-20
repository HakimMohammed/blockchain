package org.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "Exchanges",
        info = @Info(
                title = "Exchange contract",
                description = "A simple exchange contract",
                version = "0.0.1-SNAPSHOT"))

@Default
public final class ExchangeRepository implements ContractInterface {
    private final Genson genson = new Genson();
    private final InventoryRepository inventoryContract = new InventoryRepository();

    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        Exchange exchange = new Exchange("exchange1", "product1", "organization1", 100, new Date().toString(), TransactionType.SEND);

        String exchangeState = genson.serialize(exchange);
        stub.putStringState("exchange1", exchangeState);
    }

    @Transaction()
    public Exchange getExchange(final Context ctx, final String exchangeId) {
        ChaincodeStub stub = ctx.getStub();
        String exchangeState = stub.getStringState(exchangeId);

        if (exchangeState.isEmpty()) {
            String errorMessage = String.format("Exchange %s does not exist", exchangeId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Exchange not found");
        }

        return genson.deserialize(exchangeState, Exchange.class);
    }

    @Transaction()
    public Exchange createExchange(final Context ctx, final String exchangeId, final String productId, final String organization, final int quantity, final String date, final TransactionType transaction) {
        ChaincodeStub stub = ctx.getStub();
        String exchangeState = stub.getStringState(exchangeId);

        if (!exchangeState.isEmpty()) {
            String errorMessage = String.format("Exchange %s already exists", exchangeId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Exchange already exists");
        }

        inventoryContract.updateInventoryStock(ctx, organization, productId, quantity, transaction);

        Exchange exchange = new Exchange(exchangeId, productId, organization, quantity, date, transaction);
        exchangeState = genson.serialize(exchange);
        stub.putStringState(exchangeId, exchangeState);

        return exchange;
    }

    @Transaction()
    public List<Exchange> getExchangesByOrganization(final Context ctx, final String organization) {
        if (organization == null || organization.isEmpty()) {
            throw new ChaincodeException("Organization cannot be null or empty", "Invalid Input");
        }

        ChaincodeStub stub = ctx.getStub();
        List<Exchange> exchanges = new ArrayList<>();

        try (QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "")) {
            for (KeyValue result : results) {
                try {
                    Exchange exchange = genson.deserialize(result.getStringValue(), Exchange.class);
                    if (organization.equals(exchange.getOrganization())) {
                        exchanges.add(exchange);
                    }
                } catch (Exception e) {
                    System.err.println("Error deserializing state: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new ChaincodeException("Error retrieving exchanges: " + e.getMessage(), "Query Error");
        }

        return exchanges;
    }


}
