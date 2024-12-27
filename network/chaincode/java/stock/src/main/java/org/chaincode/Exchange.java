package org.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@DataType()
public final class Exchange {

    @Property()
    private final String exchange_id;

    @Property()
    private final UUID product_id;

    @Property()
    private final String organization;

    @Property()
    private final int quantity;

    @Property()
    private final String date;

    @Property
    private final TransactionType transaction;

    public String getExchange_id() {
        return exchange_id;
    }

    public UUID getProduct_id() {
        return product_id;
    }

    public String getOrganization() {
        return organization;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public TransactionType getTransaction()
    {
        return transaction;
    }

    public Exchange(@JsonProperty("exchange_id") final String exchange_id, @JsonProperty("product_id") final UUID product_id, @JsonProperty("organization") final String organization, @JsonProperty("quantity") final int quantity, @JsonProperty("date") final String date, @JsonProperty("transaction") final TransactionType transaction) {
        this.exchange_id = exchange_id;
        this.product_id = product_id;
        this.organization = organization;
        this.quantity = quantity;
        this.date = date;
        this.transaction = transaction;
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
                new String[]{getExchange_id(), getOrganization(), getDate()},
                new String[]{other.getExchange_id(), other.getOrganization(), other.getDate()}
        ) && Objects.deepEquals(
                new int[]{getQuantity()},
                new int[]{other.getQuantity()}
        ) && Objects.deepEquals(
                new TransactionType[]{getTransaction()},
                new TransactionType[]{other.getTransaction()}
        ) && Objects.deepEquals(
                new UUID[]{getProduct_id()},
                new UUID[]{other.getProduct_id()}
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExchange_id(), getProduct_id(), getOrganization(), getQuantity(), getDate(), getTransaction());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) +
                " [exchange_id=" + exchange_id + ", product_id=" + product_id + ", organization=" + organization + ", quantity=" + quantity + ", date=" + date + ", transaction=" + transaction + "]";
    }
}
