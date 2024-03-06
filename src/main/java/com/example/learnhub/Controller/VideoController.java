package com.example.learnhub.Controller;

import com.example.learnhub.Entity.Video;
import com.example.learnhub.Service.ServiceOfVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final ServiceOfVideo videoService;

    @Autowired
    public VideoController(ServiceOfVideo videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/create")
    public ResponseEntity<Video> createVideo(@RequestBody Video video) {
        Video createdVideo = videoService.createVideo(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable("id") Integer id) {
        Optional<Video> video = videoService.findVideoById(id);
        return video.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable("id") Integer id, @RequestBody Video updatedVideo) {
        Video video = videoService.updateVideo(id, updatedVideo);
        return video != null ? ResponseEntity.ok(video) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable("id") Integer id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    // Add more endpoints as needed
}
