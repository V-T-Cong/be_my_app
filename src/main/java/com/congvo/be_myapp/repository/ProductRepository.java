package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsBySlug(String slug);

    @Override
    @EntityGraph(attributePaths = {"variants"})
    List<Product> findAll();
}