package com.seafood.demo.service;

import com.seafood.demo.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Category saveCategory(Category category);
    Optional<Category> getCategoryById(Long id);
    void deleteCategory(Long id);
}
