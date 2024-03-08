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
    public ResponseEntity<String> createSection(
            @RequestParam(value = "videoFiles", required = false) List<MultipartFile> videoFiles,
            @RequestParam(value = "articleFiles", required = false) List<MultipartFile> articleFiles,
            @RequestParam("Section") SectionDTO  sectionName,
            @RequestParam("courseId") Integer courseId) {
        try {
            // Check if both video and article files are not provided
            if ((videoFiles == null || videoFiles.isEmpty()) && (articleFiles == null || articleFiles.isEmpty())) {
                return ResponseEntity.badRequest().body("At least one of video or article files is required");
            }

            // Retrieve the course by courseId
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            // Create section
            Section createdSection = sectionService.createSection(sectionName, course, videoFiles, articleFiles);

            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Section created successfully with ID: " + createdSection.getSectionId());
        } catch (AppServiceExeption | IOException e) {
            // Handle service exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create section: " + e.getMessage());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
