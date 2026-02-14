package com.congvo.be_myapp.entity;

import com.congvo.be_myapp.emuns.ProductType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="product_variants")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type; // KEY or ACCOUNT

    @Column(nullable = false)
    private BigDecimal price;

    private String variantName;

    // Helper to calculate stock quickly without joining the inventory table every time
    // You would update this count whenever InventoryItem is added/sold
    private int stockQuantity = 0;

}
