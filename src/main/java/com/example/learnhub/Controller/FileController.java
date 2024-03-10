package com.example.learnhub.Controller;

import java.io.IOException;
import java.util.List;

import com.example.learnhub.Service.ServiceOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    ServiceOfFile fileService;

    //List all file name
    @GetMapping
    public ResponseEntity<List<String>> listOfFiles() {

        List<String> files = fileService.listOfFiles();

        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Call uploadFile method from fileService and retrieve the URL
            String fileUrl = fileService.uploadFile(file);

            // Return the URL in the response
            return ResponseEntity.ok("File uploaded successfully. URL: " + fileUrl);
        } catch (IOException e) {
            // Handle file upload failure
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
    //Delete file
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteFile(
            @RequestParam String fileName) {

        fileService.deleteFile(fileName);

        return ResponseEntity.ok(" File deleted successfully");
    }

    //Download file
    @GetMapping("download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String fileName)  {

        ByteArrayResource resource = fileService.downloadFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_OCTET_STREAM).
                headers(headers).body(resource);
    }
}