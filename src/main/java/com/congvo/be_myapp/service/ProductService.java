package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.ProductRequest;
import com.congvo.be_myapp.dto.request.VariantRequest;
import com.congvo.be_myapp.dto.response.ProductResponse;
import com.congvo.be_myapp.emuns.ProductType;
import com.congvo.be_myapp.entity.Category;
import com.congvo.be_myapp.entity.Product;
import com.congvo.be_myapp.entity.ProductVariant;
import com.congvo.be_myapp.repository.CategoryRepository;
import com.congvo.be_myapp.repository.ProductRepository;
import com.congvo.be_myapp.repository.ProductVariantRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.DecimalMaxValidatorForBigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            ProductVariantRepository variantRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.categoryRepository = categoryRepository;
    }

//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRepository.existsBySlug(productRequest.getSlug())) {
            throw new RuntimeException("Product with this slug already exists");
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSlug(productRequest.getSlug());
        product.setDescription(productRequest.getDescription());
        product.setThumbnailUrl(productRequest.getThumbnailUrl());
        product.setDiscountPercent(productRequest.getDiscountPercent());
        product.setActive(true);

        if (productRequest.getCategoryIds() != null && !productRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(productRequest.getCategoryIds()));
            product.setCategories(categories);
        }

        Product savedProduct = productRepository.save(product);

        if (productRequest.getVariants() != null && !productRequest.getVariants().isEmpty()) {
            List<ProductVariant> variants = productRequest.getVariants().stream().map(variantRequest -> {
                ProductVariant variant = new ProductVariant();
                variant.setProduct(savedProduct);
                variant.setType(ProductType.valueOf(variantRequest.getType().toUpperCase()));
                variant.setPrice(variantRequest.getPrice());
                variant.setVariantName(variantRequest.getVariantName());
                variant.setDiscountPrice(variantRequest.getDiscountPrice());
                variant.setActive(true);
                variant.setStockQuantity(0);
                return variant;
            }).toList();

            variantRepository.saveAll(variants);
            savedProduct.setVariants(variants);
        }

        return new ProductResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId,ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (productRequest.getName() != null) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getSlug() != null) {
            product.setSlug(productRequest.getSlug());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getThumbnailUrl() != null) {
            product.setThumbnailUrl(productRequest.getThumbnailUrl());
        }
        if  (productRequest.getDiscountPercent() != null) {
            product.setDiscountPercent(productRequest.getDiscountPercent());
        }

        productRepository.save(product);

        return new ProductResponse(product);
    }


    @Transactional
    public ProductResponse addVariantToProduct(UUID productId, VariantRequest variantRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setType(ProductType.valueOf(variantRequest.getType().toUpperCase()));
        variant.setPrice(variantRequest.getPrice());
        variant.setVariantName(variantRequest.getVariantName());
        variant.setDiscountPrice(variantRequest.getDiscountPrice());
        variant.setActive(true);
        variant.setStockQuantity(0);

        variantRepository.save(variant);

        product.getVariants().add(variant);

        return new ProductResponse(product);
    }

    @Transactional
    public void updateVariantStatus(UUID variantId, boolean isActive) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Product Variant not found with id: " + variantId));

        variant.setActive(isActive);

        variantRepository.save(variant);
    }
}
