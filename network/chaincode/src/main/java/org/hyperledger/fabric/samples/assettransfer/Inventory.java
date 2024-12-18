package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Map;
import java.util.Objects;

@DataType
public final class Inventory {

    @Property
    private final String inventory_id;

    @Property
    private final String organization;

    @Property
    private final Map<String, Integer> stock;

    public String getOrganization() {
        return organization;
    }

    public Map<String, Integer> getStock() {
        return stock;
    }

    public String getInventory_id() {
        return inventory_id;
    }

    public Inventory(@JsonProperty("inventory_id") final String inventory_id, @JsonProperty("organization") final String organization, @JsonProperty("stock") final Map<String, Integer> stock) {
        this.inventory_id = inventory_id;
        this.organization = organization;
        this.stock = stock;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Inventory other = (Inventory) obj;
        return Objects.deepEquals(new Object[]{getOrganization(), getStock()}, new Object[]{other.getOrganization(), other.getStock()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganization(), getStock());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) +
                " [inventory_id=" + inventory_id + ", organization=" + organization + ", stock=" + stock + "]";
    }
}
