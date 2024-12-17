package org.hyperledger.fabric.samples.assettransfer;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


public final class InventoryTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            HashMap<String, Integer> stock = new HashMap<>();
            stock.put("Blue", 20);
            stock.put("Red", 40);
            Inventory inventory = new Inventory("inventory1", "organization1", stock);
            assertThat(inventory).isEqualTo(inventory);
        }

        @Test
        public void isSymmetric() {
            HashMap<String, Integer> stock1 = new HashMap<>();
            stock1.put("Blue", 20);
            stock1.put("Red", 40);

            HashMap<String, Integer> stock2 = new HashMap<>();
            stock2.put("Blue", 20);
            stock2.put("Red", 40);

            Inventory inventoryA = new Inventory("inventory1", "organization1", stock1);
            Inventory inventoryB = new Inventory("inventory1", "organization1", stock2);
            assertThat(inventoryA).isEqualTo(inventoryB);
            assertThat(inventoryB).isEqualTo(inventoryA);
        }

        @Test
        public void isTransitive() {
            HashMap<String, Integer> stock1 = new HashMap<>();
            stock1.put("Blue", 20);
            stock1.put("Red", 40);

            HashMap<String, Integer> stock2 = new HashMap<>();
            stock2.put("Blue", 20);
            stock2.put("Red", 40);

            HashMap<String, Integer> stock3 = new HashMap<>();
            stock3.put("Blue", 20);
            stock3.put("Red", 40);

            Inventory inventoryA = new Inventory("inventory1", "organization1", stock1);
            Inventory inventoryB = new Inventory("inventory1", "organization1", stock2);
            Inventory inventoryC = new Inventory("inventory1", "organization1", stock3);
            assertThat(inventoryA).isEqualTo(inventoryB);
            assertThat(inventoryB).isEqualTo(inventoryC);
            assertThat(inventoryA).isEqualTo(inventoryC);
        }

        @Test
        public void handlesInequality() {
            HashMap<String, Integer> stock1 = new HashMap<>();
            stock1.put("Blue", 40);
            stock1.put("Red", 20);

            HashMap<String, Integer> stock2 = new HashMap<>();
            stock2.put("Blue", 20);
            stock2.put("Red", 40);

            Inventory inventoryA = new Inventory("inventory1", "organization1", stock1);
            Inventory inventoryB = new Inventory("inventory1", "organization1", stock2);
            assertThat(inventoryA).isNotEqualTo(inventoryB);
        }

        @Test
        public void handlesOtherObjects() {
            HashMap<String, Integer> stock = new HashMap<>();
            stock.put("Blue", 20);
            stock.put("Red", 40);
            Inventory inventory = new Inventory("inventory1", "organization1", stock);
            String inventoryB = "not a inventory";
            assertThat(inventory).isNotEqualTo(inventoryB);
        }

        @Test
        public void handlesNull() {
            HashMap<String, Integer> stock = new HashMap<>();
            stock.put("Blue", 20);
            stock.put("Red", 40);
            Inventory inventory = new Inventory("inventory1", "organization1", stock);
            assertThat(inventory).isNotEqualTo(null);
        }
    }

    @Test
    public void toStringIdentifiesInventory() {
        HashMap<String, Integer> stock = new HashMap<>();
        stock.put("Blue", 20);
        stock.put("Red", 40);
        Inventory inventory = new Inventory("inventory1", "organization1", stock);
        assertThat(inventory.toString()).isEqualTo("Inventory@c8df5aea, organization1, stock: {Blue=20, Red=40}");
    }
}
