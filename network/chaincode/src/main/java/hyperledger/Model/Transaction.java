package hyperledger.Model;

import hyperledger.annotation.DataType;
import hyperledger.annotation.JsonProperty;
import hyperledger.annotation.Property;

import java.util.Date;
import java.util.Objects;

@DataType()
public final class Transaction {

    @Property()
    private final String transaction_id;
    
    @Property()
    private final String product_id;
    
    @Property()
    private final String category_id;
    
    @Property()
    private final int quantity;
    
    @Property()
    private final Date date;
    
    @Property()
    private final String sender_id;
    
    @Property()
    private final String receiver_id;

    public String getTransaction_id() {
        return transaction_id;
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

    public String getSender_id() {
        return sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }
    
    public Transaction(@JsonProperty("transaction_id") final String transaction_id, @JsonProperty("product_id") final String product_id,
                       @JsonProperty("category_id") final String category_id, @JsonProperty("quantity") final int quantity,
                       @JsonProperty("date") final Date date, @JsonProperty("sender_id") final String sender_id,
                       @JsonProperty("receiver_id") final String receiver_id) {
        this.transaction_id = transaction_id;
        this.product_id = product_id;
        this.category_id = category_id;
        this.quantity = quantity;
        this.date = date;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Transaction other = (Transaction) obj;
        return Objects.deepEquals(
                new String[] {getTransaction_id(), getProduct_id(), getCategory_id(), getSender_id(), getReceiver_id()},
                new String[] {other.getTransaction_id(), other.getProduct_id(), other.getCategory_id(), other.getSender_id(), other.getReceiver_id()}
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
        return Objects.hash(getTransaction_id(), getProduct_id(), getCategory_id(), getQuantity(), getDate(), getSender_id(), getReceiver_id());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [transaction_id=" + transaction_id + ", product_id=" + product_id + ", category_id=" + category_id + ", quantity=" + quantity + ", date=" + date + ", sender_id=" + sender_id + ", receiver_id=" + receiver_id + "]";
    }


}
