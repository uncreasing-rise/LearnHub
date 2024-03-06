package com.example.learnhub.Service;

import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfVideo implements IServiceOfVideo {

    private final ServiceOfFile serviceOfFile;
    private final VideoRepository videoRepository;

    @Autowired
    public ServiceOfVideo(ServiceOfFile serviceOfFile, VideoRepository videoRepository) {
        this.serviceOfFile = serviceOfFile;
        this.videoRepository = videoRepository;
    }

    @Override
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Optional<Video> findVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video updateVideo(Integer id, Video updatedVideo) {
        Optional<Video> optionalExistingVideo = videoRepository.findById(id);
        if (optionalExistingVideo.isPresent()) {
            Video existingVideo = optionalExistingVideo.get();
            existingVideo.setVideoData(updatedVideo.getVideoData());
            // Update other properties as needed...
            return videoRepository.save(existingVideo);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteVideo(Integer id) {
        videoRepository.deleteById(id);
    }

    @Override
    public void createVideos(Section section, List<MultipartFile> videoFiles) {
        if (videoFiles == null || videoFiles.isEmpty()) {
            throw new IllegalArgumentException("No video files provided");
        }

        for (MultipartFile videoFile : videoFiles) {
            if (videoFile.isEmpty()) {
                // Skip empty files
                continue;
            }

            try {
                if (isVideoFile(videoFile)) {
                    // Upload video file to GCS using ServiceOfFile
                    serviceOfFile.uploadFile(videoFile);

                    // Since uploadFile is void and does not return the videoData,
                    // you might need to retrieve or generate an identifier for the uploaded file
                    String videoData = generateIdentifier(videoFile);

                    // Create Video entity
                    Video video = new Video();
                    video.setVideoData(videoData);
                    video.setSectionID(section.getSectionId()); // Set the section

                    // Save the Video entity
                    videoRepository.save(video);
                } else {
                    throw new IllegalArgumentException("File is not a valid video file");
                }
            } catch (IOException e) {
                // Handle upload failure
                e.printStackTrace();
            }
        }
    }

    private boolean isVideoFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return StringUtils.hasText(filename) && getVideoFileExtensions().stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private List<String> getVideoFileExtensions() {
        // Add more video file extensions as needed (e.g., mkv, flv, etc.)
        return List.of(".mp4", ".avi", ".mov", ".mkv", ".flv");
    }

    // This method generates an identifier for the uploaded file
    private String generateIdentifier(MultipartFile file) {
        // Your implementation here to generate an identifier for the uploaded file
        // For example, you might use a UUID
        return java.util.UUID.randomUUID().toString();
    }
}
