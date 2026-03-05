package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.ProductRequest;
import com.congvo.be_myapp.dto.response.ProductResponse;
import com.congvo.be_myapp.emuns.ProductType;
import com.congvo.be_myapp.entity.Category;
import com.congvo.be_myapp.entity.Product;
import com.congvo.be_myapp.entity.ProductVariant;
import com.congvo.be_myapp.repository.CategoryRepository;
import com.congvo.be_myapp.repository.ProductRepository;
import com.congvo.be_myapp.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            List<ProductVariant> variants = productRequest.getVariants().stream().map(vReq -> {
                ProductVariant variant = new ProductVariant();
                variant.setProduct(savedProduct);
                variant.setType(ProductType.valueOf(vReq.getType().toUpperCase()));
                variant.setPrice(vReq.getPrice());
                variant.setVariantName(vReq.getVariantName());
                variant.setDiscountPrice(vReq.getDiscountPrice());
                variant.setActive(true);
                variant.setStockQuantity(0);
                return variant;
            }).toList();

            variantRepository.saveAll(variants);
            savedProduct.setVariants(variants);
        }

        return new ProductResponse(savedProduct);
    }

}
