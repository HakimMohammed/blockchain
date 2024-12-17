package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.*;

@Contract(
        name = "basic",
        info = @Info(
                title = "Update Inventory",
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
public class InventoryUpdate implements ContractInterface {
    private final Genson genson = new Genson();

    private enum InventoryUpdateErrors {
        INVENTORY_NOT_FOUND,
        INVENTORY_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("7363a89d-d069-47b7-951c-d3885de6b473", 100);
        map1.put("357b8612-90d9-4d0e-8803-1c741bb46c74", 200);
        putInventory(ctx, new Inventory("6aa24120-3e9f-470b-8608-86f17290a7cc", "b34eaf92-94f7-4ec2-ab45-db6ebcf677e4", map1));

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("7363a89d-d069-47b7-951c-d3885de6b473", 50);
        map2.put("357b8612-90d9-4d0e-8803-1c741bb46c74", 250);
        putInventory(ctx, new Inventory("792094e2-7b15-46d2-9881-c6774f8b2bbc", "a5639fda-7a58-4c73-a6d9-3efc1788336e", map2));

        Map<String, Integer> map3 = new HashMap<>();
        map3.put("7363a89d-d069-47b7-951c-d3885de6b473", 300);
        map3.put("357b8612-90d9-4d0e-8803-1c741bb46c74", 400);
        putInventory(ctx, new Inventory("88924827-51b9-43ec-99b8-d57aed2bd858", "f5444085-158b-4a93-80a8-d724a72983ba", map3));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Inventory CreateInventory(final Context ctx, final String inventory_id, final String organization, final Map<String, Integer> inventory) {

        if (InventoryExists(ctx, inventory_id)) {
            String errorMessage = String.format("Inventory %s already exists", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, InventoryUpdateErrors.INVENTORY_ALREADY_EXISTS.toString());
        }

        return putInventory(ctx, new Inventory(inventory_id, organization, inventory));
    }

    private Inventory putInventory(final Context ctx, final Inventory inventory) {
        // Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(inventory);
        ctx.getStub().putStringState(inventory.getInventory_id(), sortedJson);

        return inventory;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Inventory ReadInventory(final Context ctx, final String inventory_id) {
        String inventoryJSON = ctx.getStub().getStringState(inventory_id);

        if (inventoryJSON == null || inventoryJSON.isEmpty()) {
            String errorMessage = String.format("Inventory %s does not exist", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, InventoryUpdateErrors.INVENTORY_NOT_FOUND.toString());
        }

        return genson.deserialize(inventoryJSON, Inventory.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Inventory UpdateInventory(final Context ctx, final String inventory_id, final String product_id, final int quantity, final String transaction_type) {
        if(!InventoryExists(ctx, inventory_id)) {
            String errorMessage = String.format("Inventory %s does not exist", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, InventoryUpdateErrors.INVENTORY_NOT_FOUND.toString());
        }

        Inventory inventory = ReadInventory(ctx, inventory_id);
        HashMap<String, Integer> inventoryMap = new HashMap<>(inventory.getInventory());
        if( transaction_type.equals("RECIEVE") ) {
            inventoryMap.put(product_id, inventoryMap.getOrDefault(product_id, 0) + quantity);
        }
        else if( transaction_type.equals("SEND") ) {
            inventoryMap.put(product_id, inventoryMap.getOrDefault(product_id, 0) - quantity);
        }

        return putInventory(ctx, new Inventory(inventory_id, inventory.getOrganization(), inventoryMap));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteInventory(final Context ctx, final String inventory_id) {
        if (!InventoryExists(ctx, inventory_id)) {
            String errorMessage = String.format("Inventory %s does not exist", inventory_id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, InventoryUpdateErrors.INVENTORY_NOT_FOUND.toString());
        }

        ctx.getStub().delState(inventory_id);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private boolean InventoryExists(final Context ctx, final String inventory_d) {
        String assetString = ctx.getStub().getStringState(inventory_d);
        return (assetString != null && !assetString.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllInventories(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Map<String, Object>> formattedInventoryList = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Inventory inventory = genson.deserialize(result.getStringValue(), Inventory.class);

            // Custom formatting
            Map<String, Object> formattedInventory = new LinkedHashMap<>();
            formattedInventory.put(inventory.getInventory_id(), inventory);

            formattedInventoryList.add(formattedInventory);
        }

        return genson.serialize(formattedInventoryList);
    }

}
