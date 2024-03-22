package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CategoryDTO;
import com.example.learnhub.Service.ServiceOfCategory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@CrossOrigin("*")

@RestController
@RequestMapping("/api/categories")

public class CategoryController {

    private final ServiceOfCategory serviceOfCategory;

    public CategoryController(ServiceOfCategory serviceOfCategory) {
        this.serviceOfCategory = serviceOfCategory;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return serviceOfCategory.getAllCategories();
    }
}
