package com.congvo.be_myapp.controller;

import com.congvo.be_myapp.dto.request.ProductRequest;
import com.congvo.be_myapp.dto.request.VariantRequest;
import com.congvo.be_myapp.dto.request.VariantStatusRequest;
import com.congvo.be_myapp.dto.response.ProductResponse;
import com.congvo.be_myapp.entity.Product;
import com.congvo.be_myapp.repository.ProductRepository;
import com.congvo.be_myapp.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository,  ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = products.stream()
                .map(ProductResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productID}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productID));
        return ResponseEntity.ok(new ProductResponse(product));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart("product") ProductRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        ProductResponse response = productService.createProduct(request, images);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productID}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productID,
            @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(productID, productRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductResponse> addVariantToProduct(
            @PathVariable UUID productId,
            @RequestBody VariantRequest request) {
        ProductResponse response = productService.addVariantToProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/variants/{variantId}/status")
    public ResponseEntity<String> updateVariantStatus(
            @PathVariable UUID variantId,
            @RequestBody VariantStatusRequest request) {

        productService.updateVariantStatus(variantId, request.isActive());

        String statusMessage = request.isActive() ? "activated" : "deactivated";
        return ResponseEntity.ok("Product variant has been " + statusMessage + " successfully.");
    }

    @PostMapping("/{productId}/categories")
    public ResponseEntity<ProductResponse> addCategories(
            @PathVariable UUID productId,
            @RequestBody Set<UUID> categoryIds) {
        ProductResponse response = productService.addCategoriesToProduct(productId, categoryIds);
        return ResponseEntity.ok(response);
    }

}
