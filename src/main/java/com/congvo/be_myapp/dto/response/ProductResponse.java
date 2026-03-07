package com.congvo.be_myapp.dto.response;

import com.congvo.be_myapp.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String slug;
    private boolean active;
    private String description;
    private String thumbnailUrl;
    private BigDecimal finalPrice;
    private String discountLabel;
    private List<VariantDTO> variants;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.slug = product.getSlug();
        this.active = product.isActive();
        this.description = product.getDescription();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.variants = product.getVariants().stream()
                .map(variant -> {
                    VariantDTO dto = new VariantDTO();
                    dto.setId(variant.getId());
                    dto.setType(variant.getType().name());
                    dto.setPrice(variant.getPrice());
                    dto.setVariantName(variant.getVariantName());
                    dto.setStockQuantity(variant.getStockQuantity());
                    dto.setActive(variant.isActive());
                    return dto;
                }).toList();
    }

    @Data
    public static class VariantDTO {
        private UUID id;
        private String type;
        private BigDecimal price;
        private String variantName;
        private int stockQuantity;
        private boolean active;
    }
}
