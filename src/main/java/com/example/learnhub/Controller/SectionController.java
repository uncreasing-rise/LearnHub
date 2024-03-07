// SectionController.java

package com.example.learnhub.Controller;

import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Service.ServiceOfSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final ServiceOfFile serviceOfFile;
    private final ServiceOfSection sectionService;

    @Autowired
    public SectionController(ServiceOfFile serviceOfFile, ServiceOfSection sectionService) {
        this.serviceOfFile = serviceOfFile;
        this.sectionService = sectionService;
    }
    // In SectionController.java
    @PostMapping("/create")
    public ResponseEntity<String> createSection(
            @RequestParam(value = "videoFiles", required = false) List<MultipartFile> videoFiles,
            @RequestParam(value = "articleFiles", required = false) List<MultipartFile> articleFiles,
            @RequestParam("sectionName") String sectionName,
            @RequestBody(required = false) QuizDTO quizDTO) { // Add QuizDTO parameter
        try {
            // Check if both video and article files are not provided
            if ((videoFiles == null || videoFiles.isEmpty()) && (articleFiles == null || articleFiles.isEmpty()) && quizDTO == null) {
                return ResponseEntity.badRequest().body("At least one of video, article files, or a quiz is required");
            }

            // Upload video files to GCS if provided
            List<String> videoUploadedFilePaths = new ArrayList<>();
            if (videoFiles != null && !videoFiles.isEmpty()) {
                for (MultipartFile videoFile : videoFiles) {
                    serviceOfFile.uploadFile(videoFile);
                    videoUploadedFilePaths.add(serviceOfFile.constructFileUrl(videoFile.getOriginalFilename()));
                }
            }

            // Upload article files to GCS if provided
            List<String> articleUploadedFilePaths = new ArrayList<>();
            if (articleFiles != null && !articleFiles.isEmpty()) {
                for (MultipartFile articleFile : articleFiles) {
                    serviceOfFile.uploadFile(articleFile);
                    articleUploadedFilePaths.add(serviceOfFile.constructFileUrl(articleFile.getOriginalFilename()));
                }
            }

            // Create Section entity
            Section section = new Section();
            section.setSectionName(sectionName);
            // Set other properties of Section as needed

            // Save the Section entity
            sectionService.createSection(section, videoFiles, articleFiles, quizDTO); // Pass quizDTO to the service

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Section created successfully with Video and Article uploaded to GCS");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file to GCS: " + e.getMessage());
        }
    }




    // Add more endpoints as needed
}
