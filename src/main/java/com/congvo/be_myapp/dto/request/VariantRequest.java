package com.congvo.be_myapp.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariantRequest {
    private String type;
    private BigDecimal price;
    private String variantName;
    private BigDecimal discountPrice;
}
