package com.example.learnhub.Service;

import com.example.learnhub.Entity.Category;
import com.example.learnhub.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOfCategory {

    private final CategoryRepository categoryRepository;

    @Autowired
    public ServiceOfCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getAllCategoryNames() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }
}
