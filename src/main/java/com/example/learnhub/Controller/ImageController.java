package com.example.learnhub.Controller;

import com.example.learnhub.ResponeObject.ResponeObject;
import com.example.learnhub.Service.ServiceOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageController {

    @Autowired
    private ServiceOfFile serviceOfFile;

    // Your other controller methods

    @PostMapping("/saveCourseImages")
    public ResponseEntity<ResponeObject> saveCourseImages(@RequestParam("images") List<MultipartFile> images, @RequestParam int courseId) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            try {
                serviceOfFile.uploadFile(image); // Upload the file
                // Construct the URL based on where the file was saved
                String uploadedFileUrl = constructFileUrl(image);
                imageUrls.add(uploadedFileUrl);
            } catch (IOException e) {
                // Handle upload failure
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponeObject("Failed to upload one or more images"));
            }
        }

        // Create and return a response object
        ResponeObject responseObject = new ResponeObject("Images uploaded successfully", imageUrls);
        return ResponseEntity.ok(responseObject);
    }

    private String constructFileUrl(MultipartFile file) {
        // Construct the URL based on where the file was saved
        // For example, if the file was saved in a specific directory:
        // return "http://example.com/files/" + file.getOriginalFilename();
        // Since the uploadFile method is void, you'll need to decide how to generate the URL
        // based on your application's file storage configuration.
        return ""; // Placeholder URL
    }
}
