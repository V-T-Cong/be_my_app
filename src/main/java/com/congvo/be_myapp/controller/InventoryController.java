package com.congvo.be_myapp.controller;


import com.congvo.be_myapp.dto.request.InventoryAddRequest;
import com.congvo.be_myapp.dto.request.InventoryUpdateRequest;
import com.congvo.be_myapp.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody InventoryAddRequest request) {
        inventoryService.addInventoryItems(request);
        return ResponseEntity.ok("Successfully added " + request.getSecretValues().size() + " items to inventory.");
    }

    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateInventoryItem(
            @PathVariable UUID id,
            @RequestBody InventoryUpdateRequest request) {
        inventoryService.updateInventoryItem(id, request);
        return ResponseEntity.ok("Inventory item updated successfully.");
    }

}
