// SectionController.java

package com.example.learnhub.Controller;

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
    @PostMapping("/create")
    public ResponseEntity<String> createSection(
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "articleFile", required = false) MultipartFile articleFile,
            @RequestParam("sectionName") String sectionName) {
        try {
            // Check if both video and article files are not provided
            if ((videoFile == null || videoFile.isEmpty()) && (articleFile == null || articleFile.isEmpty())) {
                return ResponseEntity.badRequest().body("At least one of video or article files is required");
            }

            // Upload video file to GCS if provided
            String videoUploadedFilePath = null;
            if (videoFile != null && !videoFile.isEmpty()) {
                serviceOfFile.uploadFile(videoFile);
                videoUploadedFilePath = serviceOfFile.constructFileUrl(videoFile.getOriginalFilename());
            }

            // Upload article file to GCS if provided
            String articleUploadedFilePath = null;
            if (articleFile != null && !articleFile.isEmpty()) {
                serviceOfFile.uploadFile(articleFile);
                articleUploadedFilePath = serviceOfFile.constructFileUrl(articleFile.getOriginalFilename());
            }

            // Create Section entity
            Section section = new Section();
            section.setSectionName(sectionName);
            section.setVideoPath(videoUploadedFilePath);
            section.setArticlePath(articleUploadedFilePath);
            // Set other properties of Section as needed

            // Save the Section entity
            sectionService.createSection(section, null, null); // Pass null for videoFiles and articleFiles

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
