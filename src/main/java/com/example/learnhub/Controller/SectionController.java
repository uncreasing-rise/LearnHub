// SectionController.java

package com.example.learnhub.Controller;

import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Service.ServiceOfSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final CourseRepository courseRepository;

    private final ServiceOfSection sectionService;

    @Autowired
    public SectionController(CourseRepository courseRepository, ServiceOfSection sectionService) {
        this.courseRepository = courseRepository;
        this.sectionService = sectionService;
    }
    @PostMapping("/create")
    public ResponseEntity<Section> createSection(@RequestBody SectionDTO dto,
                                                 @RequestBody Course course,
                                                 @RequestParam("articleFiles") List<MultipartFile> articleFiles,
                                                 @RequestParam("videoFiles") List<MultipartFile> videoFiles) {
        try {
            Section createdSection = sectionService.createSection(dto, course, articleFiles, videoFiles);
            return ResponseEntity.ok(createdSection);
        } catch (AppServiceExeption | IOException e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
