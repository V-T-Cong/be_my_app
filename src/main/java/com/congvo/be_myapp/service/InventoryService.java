package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.InventoryAddRequest;
import com.congvo.be_myapp.dto.request.InventoryUpdateRequest;
import com.congvo.be_myapp.dto.response.InventoryItemResponse;
import com.congvo.be_myapp.emuns.InventoryStatus;
import com.congvo.be_myapp.entity.InventoryItem;
import com.congvo.be_myapp.entity.ProductVariant;
import com.congvo.be_myapp.repository.InventoryItemRepository;
import com.congvo.be_myapp.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductVariantRepository variantRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository,
                            ProductVariantRepository variantRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.variantRepository = variantRepository;
    }


    public List<InventoryItemResponse> getInventoryByVariant(UUID variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new RuntimeException("Product Variant not found");
        }

        List<InventoryItem> items = inventoryItemRepository.findByVariantId(variantId);

        return items.stream().map(InventoryItemResponse::new).toList();
    }


    @Transactional
    public void addInventoryItems(InventoryAddRequest request) {
        if (request.getSecretValues() == null || request.getSecretValues().isEmpty()) {
            throw new RuntimeException("No secret values provided to add to inventory.");
        }

        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Product Variant not found"));

        List<InventoryItem> items = request.getSecretValues().stream().map(secret -> {
            InventoryItem item = new InventoryItem();
            item.setVariant(variant);
            item.setSecretValue(secret);
            item.setStatus(InventoryStatus.AVAILABLE);
            return item;
        }).toList();

        inventoryItemRepository.saveAll(items);

        variant.setStockQuantity(variant.getStockQuantity() + items.size());
        variantRepository.save(variant);
    }


    @Transactional
    public void updateInventoryItem(UUID id, InventoryUpdateRequest request) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory Item not found"));

        if (request.getStatus() != null) {
            item.updateStatus(request.getStatus());
        }

        if (request.getSecretValue() != null && !request.getSecretValue().isBlank()) {
            item.setSecretValue(request.getSecretValue());
        }

        inventoryItemRepository.save(item);
    }

}
