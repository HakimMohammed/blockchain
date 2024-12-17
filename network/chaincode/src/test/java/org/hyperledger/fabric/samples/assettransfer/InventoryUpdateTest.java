package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

public final class InventoryUpdateTest {

    private static final class MockKeyValue implements KeyValue {
        private final String key;
        private final String value;

        public MockKeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() { return this.key; }

        @Override
        public String getStringValue() { return this.value; }

        @Override
        public byte[] getValue() { return this.value.getBytes(); }
    }

    private static final class MockInventoryResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> inventoryList;

        MockInventoryResultsIterator() {
            inventoryList = new ArrayList<KeyValue>();

            inventoryList.add(new MockKeyValue("inventory1",
                    "{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}"));
            inventoryList.add(new MockKeyValue("inventory2",
                    "{\"inventory_id\":\"inventory2\",\"organization\":\"organization2\",\"inventory\":{\"product3\":15,\"product4\":20}}"));
            inventoryList.add(new MockKeyValue("inventory3",
                    "{\"inventory_id\":\"inventory3\",\"organization\":\"organization3\",\"inventory\":{\"product5\":25,\"product6\":30}}"));
        }

        @Override
        public Iterator<KeyValue> iterator() { return inventoryList.iterator(); }

        @Override
        public void close() throws Exception {
            // Do nothing
        }
    }

    @Test
    public void invokeUnknownTransaction() {
        InventoryUpdate contract = new InventoryUpdate();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyNoInteractions(ctx);
    }

    @Nested
    class InvokeReadInventoryTransaction {

        @Test
        public void whenInventoryExists() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1"))
                    .thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}");

            Inventory inventory = contract.ReadInventory(ctx, "inventory1");

            assertThat(inventory).isEqualTo(new Inventory("inventory1", "organization1", Map.of("product1", 5, "product2", 10)));
        }

        @Test
        public void whenInventoryDoesNotExist() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.ReadInventory(ctx, "inventory1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Inventory inventory1 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND");
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        InventoryUpdate contract = new InventoryUpdate();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.InitLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("inventory1", "{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}");
        inOrder.verify(stub).putStringState("inventory2", "{\"inventory_id\":\"inventory2\",\"organization\":\"organization2\",\"inventory\":{\"product1\":50,\"product2\":100}}");
        inOrder.verify(stub).putStringState("inventory3", "{\"inventory_id\":\"inventory3\",\"organization\":\"organization3\",\"inventory\":{\"product1\":25,\"product2\":50}}");
    }

    @Nested
    class InvokeCreateInventoryTransaction {

        @Test
        public void whenInventoryExists() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1"))
                    .thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}");

            Throwable thrown = catchThrowable(() -> {
                contract.CreateInventory(ctx, "inventory1", "organization1", Map.of("product1", 5, "product2", 10));
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Inventory inventory1 already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_ALREADY_EXISTS");
        }

        @Test
        public void whenInventoryDoesNotExist() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("");

            Inventory inventory = contract.CreateInventory(ctx, "inventory1", "organization1", Map.of("product1", 5, "product2", 10));

            assertThat(inventory).isEqualTo(new Inventory("inventory1", "organization1", Map.of("product1", 5, "product2", 10)));
        }
    }

    @Test
    void invokeGetAllInventoriesTransaction() {
        InventoryUpdate contract = new InventoryUpdate();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockInventoryResultsIterator());

        String inventories = contract.GetAllInventories(ctx);

        assertThat(inventories).isEqualTo("[" +
                "{\"inventory1\",\"{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}}}," +
                "{\"inventory2\",\"{\"inventory_id\":\"inventory2\",\"organization\":\"organization2\",\"inventory\":{\"product3\":15,\"product4\":20}}}," +
                "{\"inventory3\",\"{\"inventory_id\":\"inventory3\",\"organization\":\"organization3\",\"inventory\":{\"product5\":25,\"product6\":30}}}]");
    }

    @Nested
    class UpdateInventoryTransaction {

        @Test
        void whenInventoryExists() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1"))
                    .thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"inventory\":{\"product1\":5,\"product2\":10}}");

            Inventory inventory = contract.UpdateInventory(ctx, "inventory1", "product1", 5, "RECIEVE");

            assertThat(inventory).isEqualTo(new Inventory("inventory1", "organization1", Map.of("product1", 10, "product2", 10)));
        }

        @Test
        void whenInventoryDoesNotExist() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.UpdateInventory(ctx, "inventory1", "product1", 5, "RECIEVE");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Inventory inventory1 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND");
        }
    }

    @Nested
    class DeleteInventoryTransaction {

        @Test
        public void whenInventoryDoesNotExist() {
            InventoryUpdate contract = new InventoryUpdate();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.DeleteInventory(ctx, "inventory1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Inventory inventory1 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND");
        }
    }

}
