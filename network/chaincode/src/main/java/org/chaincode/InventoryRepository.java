package org.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Map;

@Contract(
        name = "Inventories",
        info = @Info(
                title = "Inventory contract",
                description = "A java chaincode to manage an inventory",
                version = "0.0.1-SNAPSHOT"
        )
)

@Default
    public final class InventoryRepository implements ContractInterface {
    private final Genson genson = new Genson();

    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        Inventory inventory = new Inventory("inventory1", "organization1", Map.of("asset1", 100, "asset2", 200));

        String inventoryState = genson.serialize(inventory);
        stub.putStringState("inventory1", inventoryState);
    }

    @Transaction()
    public Inventory getInventory(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();
        String inventoryState = stub.getStringState(id);

        if (inventoryState.isEmpty()) {
            String errorMessage = String.format("Inventory %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory not found");
        }

        return genson.deserialize(inventoryState, Inventory.class);
    }

    @Transaction()
    public Inventory createInventory(final Context ctx, final String id, final String organization, final Map<String, Integer> stock) {
        ChaincodeStub stub = ctx.getStub();

        String inventoryState = stub.getStringState(id);
        if (!inventoryState.isEmpty()) {
            String errorMessage = String.format("Inventory %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory already exists");
        }

        Inventory inventory = new Inventory(id, organization, stock);
        inventoryState = genson.serialize(inventory);
        stub.putStringState(id, inventoryState);

        return inventory;
    }

    @Transaction()
    public Inventory updateInventoryStock(final Context ctx, final String id, final String asset, final int quantity, final TransactionType transactionType) {
        ChaincodeStub stub = ctx.getStub();

        String inventoryState = stub.getStringState(id);
        if (inventoryState.isEmpty()) {
            String errorMessage = String.format("Inventory %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Inventory not found");
        }

        Inventory inventory = genson.deserialize(inventoryState, Inventory.class);
        Map<String, Integer> stock = inventory.getStock();

        // if asset does not exist in inventory
        if (!stock.containsKey(asset)) {
            stock.put(asset, quantity);
        } else {
            int currentQuantity = stock.get(asset);
            if (transactionType == TransactionType.SEND) {
                if (currentQuantity < quantity) {
                    String errorMessage = String.format("Not enough %s in inventory", asset);
                    System.out.println(errorMessage);
                    throw new ChaincodeException(errorMessage, "Not enough asset in inventory");
                }
                stock.put(asset, currentQuantity - quantity);
            } else {
                stock.put(asset, currentQuantity + quantity);
            }
        }

        Inventory updatedInventory = new Inventory(id, inventory.getOrganization(), stock);
        inventoryState = genson.serialize(updatedInventory);
        stub.putStringState(id, inventoryState);

        return inventory;
    }
    }
