package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
}