package com.example.learnhub.Service;

import com.example.learnhub.DTO.CategoryDTO;
import com.example.learnhub.Entity.Category;
import com.example.learnhub.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOfCategory {


    private final CategoryRepository categoryRepository; // Assuming you have a CategoryRepository for data access



    public ServiceOfCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryDTO> getAllCategories() {
        // Retrieve all categories from the database using the repository
        List<Category> categories = categoryRepository.findAll();
        // Convert Category entities to CategoryDTOs
        return categories.stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        // Map Category entity attributes to CategoryDTO attributes
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        // Map other attributes if needed
        return categoryDTO;
    }
}
