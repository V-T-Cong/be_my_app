package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {

    List<InventoryItem> findByVariantId(UUID variantId);

}