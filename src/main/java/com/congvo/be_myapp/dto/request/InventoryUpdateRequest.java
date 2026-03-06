package com.congvo.be_myapp.dto.request;

import com.congvo.be_myapp.emuns.InventoryStatus;
import lombok.Data;

@Data
public class InventoryUpdateRequest {

    private String secretValue;
    private InventoryStatus status;

}
