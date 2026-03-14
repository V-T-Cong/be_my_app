package com.congvo.be_myapp.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private List<String> imageUrls;
    private BigDecimal discountPercent;
    private Set<UUID> categoryIds;
    private List<VariantRequest> variants;

}
