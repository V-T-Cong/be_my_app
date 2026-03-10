package com.congvo.be_myapp.entity;

import com.congvo.be_myapp.emuns.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="inventory_items")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    /**
     * Stores the actual product data.
     * If Type = KEY: "AAAA-BBBB-CCCC-DDDD"
     * If Type = ACCOUNT: JSON string like {"u": "user1", "p": "pass123"}
     * or a raw string "user1|pass123"
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String secretValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status = InventoryStatus.AVAILABLE;

    // Optimistic locking to prevent double-selling the same key
    @Version
    private Long version;

    public void markAsSold() {
        if (this.status == InventoryStatus.SOLD) {
            throw new IllegalStateException("Item is already sold!");
        }
        this.status = InventoryStatus.SOLD;
        this.variant.sellItem();
    }

    public void updateStatus(InventoryStatus newStatus) {
        if (this.status == newStatus) return;

        if (this.status == InventoryStatus.AVAILABLE) {
            this.variant.decreaseStock();
        }
        else if (newStatus == InventoryStatus.AVAILABLE) {
            this.variant.increaseStock();
        }

        this.status = newStatus;
    }

}
