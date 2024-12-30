package org.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Contract(
        name = "stock",
        info = @Info(
                title = "Stock contract",
                description = "A stock managing contract",
                version = "0.0.1-SNAPSHOT"))

@Default
public final class StockRepository implements ContractInterface {
    private final Genson genson = new Genson();
    private final UUID product1Id = UUID.fromString("09b52335-63cb-45f3-a16a-b7c92c991097");
    private final UUID product2Id = UUID.fromString("517b55cb-2484-4b0f-aff6-8451006b2bf5");
    private final UUID product3Id = UUID.fromString("5c643964-3948-42b2-99b4-138749c2f38f");
    private final UUID product4Id = UUID.fromString("a3944f05-014b-4075-b428-ffd83d8c9c99");

    private Exchange putExchange(final Context ctx, final Exchange exchange) {
        ChaincodeStub stub = ctx.getStub();
        String exchangeState = genson.serialize(exchange);
        stub.putStringState(exchange.getExchange_id(), exchangeState);
        return exchange;
    }

    private Inventory putInventory(final Context ctx, final Inventory inventory) {
        ChaincodeStub stub = ctx.getStub();
        String inventoryState = genson.serialize(inventory);
        System.out.println("Inventory being put: " + inventoryState);
        stub.putStringState(inventory.getInventory_id(), inventoryState);
        return inventory;
    }

    @Transaction()
    public void initLedger(final Context ctx) {
        putInventory(ctx, new Inventory("supplier", "supplier", new LinkedHashMap<>(Map.of(product1Id, 500, product3Id, 800 , product2Id , 1000 , product4Id , 2000))));
        putInventory(ctx, new Inventory("company", "company", new LinkedHashMap<>(Map.of(product1Id, 100, product2Id, 200 ))));
        String timestamp = "2024-01-01 00:00:00 UTC";
        putExchange(ctx, new Exchange("exchange1", product1Id, "supplier", 100, timestamp, TransactionType.SEND));
        putExchange(ctx, new Exchange("exchange2", product1Id, "company", 100, timestamp, TransactionType.RECEIVE));
        putExchange(ctx, new Exchange("exchange3", product3Id, "supplier", 200, timestamp, TransactionType.SEND));
        putExchange(ctx, new Exchange("exchange4", product2Id, "company", 200, timestamp, TransactionType.RECEIVE));
    }

    @Transaction()
    public void test(final Context ctx) {
        System.out.println("Test Started");
        String timestamp = "2024-01-01 00:00:00 UTC";
        Exchange exchange = createExchange(ctx, "exchange-test-1", product1Id, "supplier1", 100, timestamp, TransactionType.SEND);
        System.out.println("Exchange created: " + exchange);
        System.out.println("Test FInished");
    }

    @Transaction()
    public boolean inventoryExists(final Context ctx, final String inventory_id) {
        ChaincodeStub stub = ctx.getStub();
        String inventoryState = stub.getStringState(inventory_id);
        return !inventoryState.isEmpty();
    }

    @Transaction()
    public boolean exchangeExists(final Context ctx, final String exchange_id) {
        ChaincodeStub stub = ctx.getStub();
        String exchangeState = stub.getStringState(exchange_id);
        return exchangeState != null && !exchangeState.isEmpty();
    }

    @Transaction()
    public String getExchangesByOrganization(final Context ctx, final String organization) {
        if (organization == null || organization.isEmpty()) {
            throw new ChaincodeException("Organization cannot be null or empty", "Invalid Input");
        }

        ChaincodeStub stub = ctx.getStub();
        List<Exchange> exchanges = new ArrayList<>();

        try (QueryResultsIterator<KeyValue> results = stub.getStateByRange("exchange", "exchange\uffff")) {
            for (KeyValue result : results) {
                try {
                    Exchange exchange = genson.deserialize(result.getStringValue(), Exchange.class);
                    if (exchange != null && organization.equals(exchange.getOrganization())) {
                        exchanges.add(exchange);
                    }
                } catch (Exception e) {
                    System.err.println("Error deserializing state: " + result.getStringValue() + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            String errorMessage = String.format("Error getting exchanges for organization %s", organization);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Error getting exchanges");
        }

        return genson.serialize(exchanges);
    }

    @Transaction()
    public boolean organizationHasStock(final Context ctx, final String organization, final UUID product_id, final int quantity) {
        Inventory inventory = getInventory(ctx, organization);

        if (inventory.getOrganization().equals(organization)) {
            Integer stock = inventory.getStock().get(product_id);
            return stock != null && stock >= quantity;
        }

        return false;
    }

    @Transaction()
    public Inventory getInventory(final Context ctx, final String inventory_id) {
        ChaincodeStub stub = ctx.getStub();
        String inventoryState = stub.getStringState(inventory_id);

        if (inventoryState.isEmpty()) {
            String errorMessage = String.format("Inventory %s does not exist", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory not found");
        }

        return genson.deserialize(inventoryState, Inventory.class);
    }

    @Transaction()
    public Exchange getExchange(final Context ctx, final String exchange_id) {
        ChaincodeStub stub = ctx.getStub();
        String exchangeState = stub.getStringState(exchange_id);

        if (exchangeState.isEmpty()) {
            String errorMessage = String.format("Exchange %s does not exist", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Exchange not found");
        }

        return genson.deserialize(exchangeState, Exchange.class);
    }

    @Transaction()
    public Inventory createInventory(final Context ctx, final String inventory_id, final String organization, final Map<UUID, Integer> stock) {

        if (!inventoryExists(ctx, inventory_id)) {
            String errorMessage = String.format("Inventory %s already exists", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory already exists");
        }

        return putInventory(ctx, new Inventory(inventory_id, organization, stock));
    }

    @Transaction()
    public Exchange createExchange(final Context ctx, final String exchange_id, final UUID product_id, final String organization, final int quantity, final String date, final TransactionType transaction) {

        if (exchangeExists(ctx, exchange_id)) {
            String errorMessage = String.format("Exchange %s already exists", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Exchange already exists");
        }

        updateInventoryStock(ctx, organization, product_id, quantity, transaction);

        return putExchange(ctx, new Exchange(exchange_id, product_id, organization, quantity, date, transaction));
    }

    @Transaction()
    public void updateInventoryStock(final Context ctx, final String inventory_id, final UUID product_id, final int quantity, final TransactionType transactionType) {
        ChaincodeStub stub = ctx.getStub();

        if (!inventoryExists(ctx, inventory_id)) {
            String errorMessage = String.format("Inventory %s does not exist", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory not found");
        }

        String inventoryState = stub.getStringState(inventory_id);
        Inventory inventory = genson.deserialize(inventoryState, Inventory.class);
        Map<UUID, Integer> stock = inventory.getStock();

        // if product_id does not exist in inventory
        if (!stock.containsKey(product_id)) {
            if (transactionType == TransactionType.SEND) {
                String errorMessage = String.format("Product %s does not exist in inventory", product_id);
                System.out.println(errorMessage);
                throw new ChaincodeException(errorMessage, "product_id does not exist in inventory");
            } else {
                stock.put(product_id, quantity);
            }
        } else {
            int currentQuantity = stock.get(product_id);
            if (transactionType == TransactionType.SEND) {
                if (currentQuantity < quantity) {
                    String errorMessage = String.format("Not enough %s in inventory", product_id);
                    System.out.println(errorMessage);
                    throw new ChaincodeException(errorMessage, "Not enough product_id in inventory");
                }
                stock.put(product_id, currentQuantity - quantity);
            } else {
                stock.put(product_id, currentQuantity + quantity);
            }
        }

        putInventory(ctx, new Inventory(inventory_id, inventory.getOrganization(), stock));
    }

    @Transaction()
    private int getExchangeCount(final Context ctx) throws Exception {
        int count = 0;
        ChaincodeStub stub = ctx.getStub();

        try (QueryResultsIterator<KeyValue> results = stub.getStateByRange("exchange", "exchange\uffff")) {
            for (KeyValue result : results) {
                count++;
            }
        }

        return count;
    }

    @Transaction()
    public void trade(final Context ctx, final String sender, final String receiver, final String product_id, final int quantity) throws Exception {

        UUID product = UUID.fromString(product_id);
        System.out.println("Trading " + quantity + " of " + product_id + " from " + sender + " to " + receiver);

        String timestamp = "2024-01-01 00:00:00 UTC";
        int exchangeCounter = getExchangeCount(ctx);
        String exchangeIdBase = String.format("%s-%s-%s-%d-%d", sender, receiver, product_id, quantity, exchangeCounter);
        createExchange(ctx, "exchange-send-" + exchangeIdBase, product, sender, quantity, timestamp, TransactionType.SEND);
        createExchange(ctx, "exchange-recv-" + exchangeIdBase, product, receiver, quantity, timestamp, TransactionType.RECEIVE);
    }
}