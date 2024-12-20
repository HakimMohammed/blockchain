
import org.chaincode.Inventory;
import org.chaincode.InventoryRepository;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class InventoryRepositoryTest {

    @Nested
    class InvokeQueryInventoryTransaction {

        @Test
        void whenInventoryExists() {
            // given
            InventoryRepository contract = new InventoryRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("{\"inventory_id\":\"inventory1\",\"organization\":\"organization1\",\"stock\":{\"asset1\":100,\"asset2\":200}}");

            Inventory inventory = contract.getInventory(ctx, "inventory1");

            assertThat(inventory.getInventory_id()).isEqualTo("inventory1");
            assertThat(inventory.getOrganization()).isEqualTo("organization1");
            assertThat(inventory.getStock()).isEqualTo(Map.of("asset1", 100, "asset2", 200));
        }

        @Test
        void whenInventoryDoesNotExist() {
            // given
            InventoryRepository contract = new InventoryRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("inventory1")).thenReturn("");

            // when
            Throwable throwable = catchThrowable(() -> contract.getInventory(ctx, "inventory1"));

            // then
            assertThat(throwable).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Inventory inventory1 does not exist");
        }
    }
}
