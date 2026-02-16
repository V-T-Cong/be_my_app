package com.congvo.be_myapp.controller;

import java.util.List;
import java.util.UUID;
import com.congvo.be_myapp.dto.request.CategoryRequest;
import com.congvo.be_myapp.entity.Category;
import com.congvo.be_myapp.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Category> addCategory(CategoryRequest categoryRequest) {
        return ResponseEntity.ok(this.categoryService.createCategory(categoryRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
    }
}
