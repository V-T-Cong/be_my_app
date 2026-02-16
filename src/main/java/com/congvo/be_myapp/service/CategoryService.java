package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.CategoryRequest;
import com.congvo.be_myapp.entity.Category;
import com.congvo.be_myapp.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryRequest categoryRequest) {

        Category savedCategory = new Category();
        savedCategory.setName(categoryRequest.getName());
        savedCategory.setDescription(categoryRequest.getDescription());
        savedCategory.setColor(categoryRequest.getColor());

        return categoryRepository.save(savedCategory);

    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category getCategoryByID(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category updateCategory(UUID id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (category.getName() != null) {
            category.setName(categoryRequest.getName());
        }
        if (category.getDescription() != null) {
            category.setDescription(categoryRequest.getDescription());
        }
        if (category.getColor() != null) {
            category.setColor(categoryRequest.getColor());
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }

    public Page<Category> getCategoriesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return categoryRepository.findAll(pageable);
    }
}
