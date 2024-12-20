import org.chaincode.Exchange;
import org.chaincode.ExchangeRepository;
import org.chaincode.TransactionType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ExchangeRepositoryTest {

    @Nested
    class InvokeQueryExchangeTransaction {

        @Test
        public void whenExchangeExists() {
            ExchangeRepository contract = new ExchangeRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("exchange1")).thenReturn(
                    "{\"exchange_id\":\"exchange1\",\"product_id\":\"product1\",\"organization\":\"organization1\",\"quantity\":100,\"date\":\"2021-07-01T00:00:00.000Z\",\"transaction\":\"SEND\"}"
            );

            Exchange exchange = contract.getExchange(ctx, "exchange1");

            assertThat(exchange.getExchange_id()).isEqualTo("exchange1");
            assertThat(exchange.getProduct_id()).isEqualTo("product1");
            assertThat(exchange.getOrganization()).isEqualTo("organization1");
            assertThat(exchange.getQuantity()).isEqualTo(100);
            assertThat(exchange.getDate()).isEqualTo("2021-07-01T00:00:00.000Z");
            assertThat(exchange.getTransaction()).isEqualTo(TransactionType.SEND);
        }

        @Test
        public void whenExchangeDoesNotExist() {
            ExchangeRepository contract = new ExchangeRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("exchange1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.getExchange(ctx, "exchange1");
            });

            assertThat(thrown)
                    .isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Exchange exchange1 does not exist");
        }
    }
}
