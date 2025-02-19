package org.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

@DataType
public final class Inventory {

    @Property()
    private final String inventory_id;

    @Property()
    private final String organization;

    @Property()
    private final Map<UUID, Integer> stock;

    public String getInventory_id() {
        return inventory_id;
    }

    public String getOrganization() {
        return organization;
    }

    public Map<UUID, Integer> getStock() {
        return stock;
    }

    public Inventory(@JsonProperty("inventory_id") final String inventory_id, @JsonProperty("organization") final String organization, @JsonProperty("stock") final Map<UUID, Integer> stock) {
        this.inventory_id = inventory_id;
        this.organization = organization;
        this.stock = new TreeMap<>(stock);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Inventory that = (Inventory) o;
        return inventory_id.equals(that.inventory_id) && organization.equals(that.organization) && stock.equals(that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventory_id(), getOrganization(), getStock());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [inventory_id=" + inventory_id + ", organization=" + organization + ", stock=" + stock + "]";
    }
}
