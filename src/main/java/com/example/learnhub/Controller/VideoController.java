package com.example.learnhub.Controller;

import com.example.learnhub.Entity.Video;
import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Service.ServiceOfVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final ServiceOfVideo videoService;
    private final ServiceOfFile fileService; // Inject the ServiceOfFile bean

    @Autowired
    public VideoController(ServiceOfVideo videoService, ServiceOfFile fileService) {
        this.videoService = videoService;
        this.fileService = fileService; // Initialize the fileService bean
    }

    @PostMapping("/create")
    public ResponseEntity<Video> createVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        // Check if file is provided
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Upload video file to Google Cloud Storage
            fileService.uploadFile(file);

            // Construct the file path in GCS
            String gcsPath = "gs://" + "learnhub_bucket" + "/" + file.getOriginalFilename();

            // Create Video entity
            Video video = new Video();
            video.setTitle(title);
            video.setDescription(description);
            video.setVideoData(gcsPath); // Set GCS URI as video data

            // Save the Video entity
            Video createdVideo = videoService.createVideo(video);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null or handle the error appropriately
        }
    }

}