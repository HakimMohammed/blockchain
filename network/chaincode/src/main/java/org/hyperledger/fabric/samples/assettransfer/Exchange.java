package org.hyperledger.fabric.samples.assettransfer;

import java.util.Date;
import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.samples.assettransfer.Types.ExchangeType;

@DataType()
public final class Exchange {

    @Property()
    private final String exchange_id;

    @Property()
    private final String product_id;

    @Property()
    private final String category_id;

    @Property()
    private final int quantity;

    @Property()
    private final Date date;

    @Property()
    private final ExchangeType exchangeType;

    @Property()
    private final String organization;

    public String getExchange_id() {
        return exchange_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public String getOrganization() {
        return organization;
    }


    public Exchange(@JsonProperty("exchange_id") final String exchange_id, @JsonProperty("product_id") final String product_id,
                    @JsonProperty("category_id") final String category_id, @JsonProperty("quantity") final int quantity,
                    @JsonProperty("date") final Date date, @JsonProperty("exchangeType") final ExchangeType exchangeType,
                    @JsonProperty("organization") final String organization) {
        this.exchange_id = exchange_id;
        this.product_id = product_id;
        this.category_id = category_id;
        this.quantity = quantity;
        this.date = date;
        this.exchangeType = exchangeType;
        this.organization = organization;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Exchange other = (Exchange) obj;
        return Objects.deepEquals(
                new String[] {getExchange_id(), getProduct_id(), getCategory_id(), getExchangeType().toString(), getOrganization()},
                new String[] {other.getExchange_id(), other.getProduct_id(), other.getCategory_id(), other.getExchangeType().toString(), getOrganization()}
        ) && Objects.deepEquals(
                new int[] {getQuantity()},
                new int[] {other.getQuantity()}
        ) && Objects.deepEquals(
                new Date[] {getDate()},
                new Date[] {other.getDate()}
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExchange_id(), getProduct_id(), getCategory_id(), getQuantity(), getDate(), getExchangeType(), getOrganization());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [transaction_id=" + exchange_id + ", product_id=" + product_id + ", category_id=" + category_id + ", quantity=" + quantity + ", date=" + date + ", type of exchange=" + exchangeType.toString() + ", organization="+ organization + "]";
    }


}