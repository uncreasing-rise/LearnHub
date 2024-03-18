package com.example.learnhub.Controller;

import com.example.learnhub.Service.ServiceOfCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ServiceOfCategory serviceOfCategory;

    @Autowired
    public CategoryController(ServiceOfCategory serviceOfCategory) {
        this.serviceOfCategory = serviceOfCategory;
    }

    @GetMapping("/names")
    public List<String> getAllCategoryNames() {
        return serviceOfCategory.getAllCategoryNames();
    }
}
