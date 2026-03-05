package com.congvo.be_myapp.dto.response;

import com.congvo.be_myapp.emuns.InventoryStatus;
import com.congvo.be_myapp.entity.InventoryItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InventoryItemResponse {

    private UUID id;
    private String secretValue;
    private InventoryStatus status;
    private LocalDateTime createdAt;

    public InventoryItemResponse(InventoryItem item) {
        this.id = item.getId();
        this.secretValue = item.getSecretValue();
        this.status = item.getStatus();
        this.createdAt = item.getCreatedAt();
    }

}
