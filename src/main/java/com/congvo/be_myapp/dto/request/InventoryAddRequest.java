package com.congvo.be_myapp.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class InventoryAddRequest {

    private UUID variantId;
    private List<String> secretValues;

}
