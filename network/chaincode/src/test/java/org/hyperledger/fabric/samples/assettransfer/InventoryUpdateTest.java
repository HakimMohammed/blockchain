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
                    "{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"stock\":{\"product1\":5,\"product2\":10}}"));
            inventoryList.add(new MockKeyValue("inventory2",
                    "{\"inventory_id\":\"inventory2\",\"organization\":\"organization2\",\"stock\":{\"product3\":15,\"product4\":20}}"));
            inventoryList.add(new MockKeyValue("inventory3",
                    "{\"inventory_id\":\"inventory3\",\"organization\":\"organization3\",\"stock\":{\"product5\":25,\"product6\":30}}"));
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
                    .thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"stock\":{\"product1\":5,\"product2\":10}}");

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
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND".getBytes());
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
        inOrder.verify(stub).putStringState("6aa24120-3e9f-470b-8608-86f17290a7cc",
                "{\"inventory_id\":\"6aa24120-3e9f-470b-8608-86f17290a7cc\"," +
                "\"organization\":\"b34eaf92-94f7-4ec2-ab45-db6ebcf677e4\"," +
                "\"stock\":{\"" +
                        "357b8612-90d9-4d0e-8803-1c741bb46c74\":200," +
                        "\"7363a89d-d069-47b7-951c-d3885de6b473\":100" +
                        "}}");
        inOrder.verify(stub).putStringState("792094e2-7b15-46d2-9881-c6774f8b2bbc",
                "{\"inventory_id\":\"792094e2-7b15-46d2-9881-c6774f8b2bbc\"," +
                        "\"organization\":\"a5639fda-7a58-4c73-a6d9-3efc1788336e\"," +
                        "\"stock\":{\"" +
                        "357b8612-90d9-4d0e-8803-1c741bb46c74\":250," +
                        "\"7363a89d-d069-47b7-951c-d3885de6b473\":50" +
                        "}}");
        inOrder.verify(stub).putStringState("88924827-51b9-43ec-99b8-d57aed2bd858",
                "{\"inventory_id\":\"88924827-51b9-43ec-99b8-d57aed2bd858\"," +
                        "\"organization\":\"f5444085-158b-4a93-80a8-d724a72983ba\"," +
                        "\"stock\":{\"" +
                        "357b8612-90d9-4d0e-8803-1c741bb46c74\":400," +
                        "\"7363a89d-d069-47b7-951c-d3885de6b473\":300" +
                        "}}");
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
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_ALREADY_EXISTS".getBytes());
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
                "{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"stock\":{\"product2\":10,\"product1\":5}}," +
                "{\"inventory_id\":\"inventory2\",\"organization\":\"organization2\",\"stock\":{\"product4\":20,\"product3\":15}}," +
                "{\"inventory_id\":\"inventory3\",\"organization\":\"organization3\",\"stock\":{\"product6\":30,\"product5\":25}}]");
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
                    .thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"stock\":{\"product1\":5,\"product2\":10}}");

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
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND".getBytes());
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
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INVENTORY_NOT_FOUND".getBytes());
        }
    }

}
