package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.samples.assettransfer.Types.ExchangeType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public final class ExchangeTest {

    @Nested
    class Equality {

         @Test
         public void isReflexive() {
             Exchange exchange = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             assertThat(exchange).isEqualTo(exchange);
         }

         @Test
         public void isSymmetric() {
             Exchange exchange1 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             Exchange exchange2 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             assertThat(exchange1).isEqualTo(exchange2);
             assertThat(exchange2).isEqualTo(exchange1);
         }

         @Test
         public void isTransitive() {
             Exchange exchange1 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             Exchange exchange2 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             Exchange exchange3 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             assertThat(exchange1).isEqualTo(exchange2);
             assertThat(exchange2).isEqualTo(exchange3);
             assertThat(exchange1).isEqualTo(exchange3);
         }

         @Test
         public void handlesInequality() {
             Exchange exchange1 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
             Exchange exchange2 = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.RECIEVE, "organization");
             assertThat(exchange1).isNotEqualTo(exchange2);
         }
    }

    /*@Test
    public void toStringIdentifiesExchange() {
        // can't predict the exact value of Date
        // so we can't use assertEquals
        Exchange exchange = new Exchange("exchange_id", "product_id", "category_id", 1, new Date(), ExchangeType.SEND, "organization");
        assertEquals(exchange.toString(), "Exchange@ac8309b0 [transaction_id=exchange_id, product_id=product_id, category_id=category_id, quantity=1, date=" + new Date() + ", type of exchange=SEND, organization=organization]");
    }*/
}
