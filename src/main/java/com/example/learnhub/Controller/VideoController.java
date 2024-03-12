package com.example.learnhub.Controller;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.SectionRepository;
import com.example.learnhub.Service.ServiceOfVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final ServiceOfVideo serviceOfVideo;
    private final SectionRepository sectionRepository;

    @Autowired
    public VideoController(ServiceOfVideo serviceOfVideo, SectionRepository sectionRepository) {
        this.serviceOfVideo = serviceOfVideo;
        this.sectionRepository = sectionRepository;
    }
    @PutMapping("/update/{videoId}/file")
    public ResponseEntity<Video> updateVideoFile(
            @PathVariable("videoId") Integer videoId,
            @RequestPart("videoFile") MultipartFile videoFile
    ) {
        try {
            Video updatedVideo = serviceOfVideo.updateVideoFile(videoId, videoFile);
            return ResponseEntity.ok(updatedVideo);
        } catch (IllegalArgumentException e) {
            // Handle invalid input or video not found
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/update/{videoId}/content")
    public ResponseEntity<Video> updateVideoContent(
            @PathVariable("videoId") Integer videoId,
            @RequestBody VideoDTO videoDTO
    ) {
        try {
            // Update the video content
            Video updatedVideo = serviceOfVideo.updateVideoContent(videoId, videoDTO);
            return ResponseEntity.ok(updatedVideo);
        } catch (IllegalArgumentException e) {
            // Handle invalid input or video not found
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/{sectionId}/create")
    public ResponseEntity<Video> createVideoToSection(
            @PathVariable("sectionId") Integer sectionId,
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestBody VideoDTO videoDTO
    ) {
        try {
            if (videoFile == null || videoDTO == null) {
                throw new IllegalArgumentException("Video file or DTO is null");
            }

            Video createdVideo = serviceOfVideo.createVideoToSection(sectionId, videoFile, videoDTO);
            return ResponseEntity.ok(createdVideo);
        } catch (IllegalArgumentException e) {
            // Handle invalid input
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{sectionId}/delete/{videoId}")
    public ResponseEntity<Void> deleteVideoFromSection(
            @PathVariable("sectionId") Integer sectionId,
            @PathVariable("videoId") Integer videoId
    ) {
        boolean deleted = serviceOfVideo.deleteVideoFromSection(sectionId, videoId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
