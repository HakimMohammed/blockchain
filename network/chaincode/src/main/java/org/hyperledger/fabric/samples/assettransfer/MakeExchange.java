package org.hyperledger.fabric.samples.assettransfer;


import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.samples.assettransfer.Types.ExchangeType;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "basic",
        info = @Info(
                title = "Make Exchange",
                description = "The hyperlegendary transaction",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "three.clowns@enset.com",
                        name = "Three Clowns",
                        url = "https://threeclowns.enset.com")))
@Default
public class MakeExchange implements ContractInterface {
    private final Genson genson = new Genson();

    private enum MakeExchangeErrors {
        EXCHANGE_NOT_FOUND,
        EXCHANGE_ALREADY_EXISTS
    }
    /**
     * Creates some initial assets on the ledger.
     * Mock history
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        putExchange(ctx, new Exchange("0ca0321e-2c4a-44c6-ae57-cb17f9dfab84", "7363a89d-d069-47b7-951c-d3885de6b473", "category1", 100, new Date(), ExchangeType.SEND, "b34eaf92-94f7-4ec2-ab45-db6ebcf677e4"));
        putExchange(ctx, new Exchange("92808588-2c9a-43bb-b482-ccb480bf8c7e", "357b8612-90d9-4d0e-8803-1c741bb46c74", "category2", 200, new Date(), ExchangeType.RECIEVE, "a5639fda-7a58-4c73-a6d9-3efc1788336e"));
        putExchange(ctx, new Exchange("6d6e24f5-7bb7-4c7c-b275-327bd6757233", "7363a89d-d069-47b7-951c-d3885de6b473", "category3", 300, new Date(), ExchangeType.SEND, "b34eaf92-94f7-4ec2-ab45-db6ebcf677e4"));
        putExchange(ctx, new Exchange("6a50c585-8443-45bc-bfb5-b54b8df0a030", "357b8612-90d9-4d0e-8803-1c741bb46c74", "category4", 400, new Date(), ExchangeType.RECIEVE, "a5639fda-7a58-4c73-a6d9-3efc1788336e"));
        putExchange(ctx, new Exchange("bbb7456f-e748-4d11-8caf-a0fcae8380fe", "7363a89d-d069-47b7-951c-d3885de6b473", "category5", 500, new Date(), ExchangeType.SEND, "f5444085-158b-4a93-80a8-d724a72983ba"));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Exchange createExchange(final Context ctx, final String exchange_id, final String product_id, final String category_id, final int quantity, final Date date, final ExchangeType exchange_type, final String organization) {

        if (ExchangeExists(ctx, exchange_id)) {
            String errorMessage = String.format("Exchange %s already exists", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MakeExchangeErrors.EXCHANGE_ALREADY_EXISTS.toString());
        }

        return putExchange(ctx, new Exchange(exchange_id, product_id, category_id, quantity, date, exchange_type, organization));
    }

    private Exchange putExchange(final Context ctx, final Exchange exchange) {
        // Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(exchange);
        ctx.getStub().putStringState(exchange.getExchange_id(), sortedJson);

        return exchange;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Exchange ReadExchange(final Context ctx, final String exchange_id) {
        String assetJSON = ctx.getStub().getStringState(exchange_id);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Exchange %s does not exist", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MakeExchangeErrors.EXCHANGE_NOT_FOUND.toString());
        }

        return genson.deserialize(assetJSON, Exchange.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Exchange UpdateExchange(final Context ctx, final String exchange_id, final String product_id, final String category_id, final int quantity, final Date date, final ExchangeType exchange_type, final String organization) {
        if (!ExchangeExists(ctx, exchange_id)) {
            String errorMessage = String.format("Exchange %s does not exist", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MakeExchangeErrors.EXCHANGE_NOT_FOUND.toString());
        }

        return putExchange(ctx, new Exchange(exchange_id, product_id, category_id, quantity, date, exchange_type, organization));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteExchange(final Context ctx, final String exchange_id) {
        if (!ExchangeExists(ctx, exchange_id)) {
            String errorMessage = String.format("Exchange %s does not exist", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MakeExchangeErrors.EXCHANGE_NOT_FOUND.toString());
        }

        ctx.getStub().delState(exchange_id);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean ExchangeExists(final Context ctx, final String exchange_id) {
        String assetJSON = ctx.getStub().getStringState(exchange_id);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String NewExchange(final Context ctx, final String exchange_id, final int quantity, final String organization, final ExchangeType exchange_type) {
        String ExchangeJSON = ctx.getStub().getStringState(exchange_id);

        if (ExchangeJSON == null || ExchangeJSON.isEmpty()) {
            String errorMessage = String.format("Exchange %s does not exist", exchange_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MakeExchangeErrors.EXCHANGE_NOT_FOUND.toString());
        }

        Exchange exchange = genson.deserialize(ExchangeJSON, Exchange.class);
        InventoryUpdate inventoryUpdate = new InventoryUpdate();


        // we will put two transaction in the blockchain SEND and RECIEVE
        switch (exchange_type) {
            case SEND:
                putExchange(ctx, new Exchange("new_exchange_id", exchange.getProduct_id(), exchange.getCategory_id(), quantity, new Date(), exchange_type, organization));
                inventoryUpdate.UpdateInventory(ctx, exchange.getOrganization(), exchange.getProduct_id(), quantity, "SEND");
                break;
            case RECIEVE:
                putExchange(ctx, new Exchange("new_exchange_id", exchange.getProduct_id(), exchange.getCategory_id(), exchange.getQuantity() + quantity, new Date(), exchange_type, organization));
                inventoryUpdate.UpdateInventory(ctx, exchange.getOrganization(), exchange.getProduct_id(), quantity, "RECIEVE");
                break;
        }
        return "Exchange created";

    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getAllExchanges(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Inventory> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for(KeyValue result: results) {
            Inventory inventory = genson.deserialize(result.getStringValue(), Inventory.class);
            System.out.println(inventory);
            queryResults.add(inventory);
        }

        return genson.serialize(queryResults);
    }

}
