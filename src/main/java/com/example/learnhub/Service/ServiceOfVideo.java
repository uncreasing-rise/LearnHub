package com.example.learnhub.Service;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.SectionRepository;
import com.example.learnhub.Repository.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfVideo implements IServiceOfVideo {


    private final ServiceOfFile serviceOfFile;
    private final VideoRepository videoRepository;
    private final SectionRepository sectionRepository;

    public ServiceOfVideo(ServiceOfFile serviceOfFile, VideoRepository videoRepository, SectionRepository sectionRepository) {
        this.serviceOfFile = serviceOfFile;
        this.videoRepository = videoRepository;
        this.sectionRepository = sectionRepository;
    }


    @Override
    public Video saveVideo(VideoDTO dto) {
        Video video = new Video();
        video.setTitle(dto.getTitle());
        video.setSection(dto.getSection());
        video.setVideoData(dto.getVideoUrl());
        video.setDescription(dto.getDescription());
        video.setIsTrial(dto.getIsTrial());
        return videoRepository.save(video);
    }


    // Controller or Service Layer
    public List<Video> createVideos(Section section, List<MultipartFile> videoFiles, List<VideoDTO> videoDTOs) {
        List<Video> videos = new ArrayList<>();

        if (videoFiles == null || videoFiles.isEmpty() || videoDTOs == null || videoDTOs.isEmpty() || videoFiles.size() != videoDTOs.size()) {
            throw new IllegalArgumentException("Invalid video files or DTOs provided");
        }

        try {
            for (int i = 0; i < videoFiles.size(); i++) {
                MultipartFile videoFile = videoFiles.get(i);
                VideoDTO videoDTO = videoDTOs.get(i);

                if (isVideoFile(videoFile)) {
                    // Upload video file to GCS using ServiceOfFile
                    String videoFilePath = serviceOfFile.uploadFile(videoFile);

                    // Create Video entity
                    Video video = new Video();
                    video.setSection(section); // Set the section ID
                    video.setVideoData(constructFileUrl(videoFilePath)); // Set the file path
                    video.setTitle(videoDTO.getTitle());
                    video.setDescription(videoDTO.getDescription());
                    video.setIsTrial(videoDTO.getIsTrial());

                    // Save the Video entity
                    videos.add(videoRepository.save(video));
                } else {
                    throw new IllegalArgumentException("File is not a valid video file");
                }
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
        }

        return videos;
    }

    private boolean isVideoFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return StringUtils.hasText(filename) && getArticleFileExtensions().stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private List<String> getArticleFileExtensions() {
        // Add more video file extensions as needed (e.g., mkv, flv, etc.)
        return List.of(".mp4", ".mp3", ".doc", ".docx");
    }

    public String constructFileUrl(String originalFilename) {
        // Trả về URL công khai cho file
        return "https://storage.googleapis.com/" + "learnhub_bucket" + "/" + originalFilename;
    }


    @Override
    public List<Video> getVideos() {
        return null;
    }


    @Transactional
    public Video updateVideoFile(int videoId, MultipartFile videoFile) {
        try {
            // Retrieve the video by its ID
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video existingVideo = optionalVideo.get();

                // Check if the file is provided and is a valid video file
                if (videoFile != null && isVideoFile(videoFile)) {
                    // Upload the video file to GCS and get the file path
                    String videoFilePath = serviceOfFile.uploadFile(videoFile);
                    // Update the video URL with the new file path
                    existingVideo.setVideoData(constructFileUrl(videoFilePath));
                } else {
                    throw new IllegalArgumentException("Invalid or missing video file");
                }

                // Save the updated video
                return videoRepository.save(existingVideo);
            } else {
                throw new IllegalArgumentException("Video not found for ID: " + videoId);
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
            throw new RuntimeException("Failed to update video file: " + e.getMessage());
        }
    }


    @Transactional
    public Video updateVideoContent(int videoId, VideoDTO videoDTO) {
        try {
            // Retrieve the video by its ID
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video existingVideo = optionalVideo.get();
                // Update video properties
                existingVideo.setTitle(videoDTO.getTitle());
                existingVideo.setDescription(videoDTO.getDescription());
                existingVideo.setIsTrial(videoDTO.getIsTrial());
                // Save the updated video
                return videoRepository.save(existingVideo);
            } else {
                throw new IllegalArgumentException("Video not found for ID: " + videoDTO.getVideoId());
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            throw new RuntimeException("Failed to update video content: " + e.getMessage());
        }
    }

    public Video createVideoToSection(int sectionId, MultipartFile videoFile, VideoDTO videoDTO) {
        // Retrieve the section from the database
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found for ID: " + sectionId));

        // Check if the video file and DTO are provided
        if (videoFile == null || videoDTO == null) {
            throw new IllegalArgumentException("Invalid video file or DTO provided");
        }

        try {
            // Check if the file is a valid video file
            if (isVideoFile(videoFile)) {
                // Upload video file to GCS using ServiceOfFile
                String videoFilePath = serviceOfFile.uploadFile(videoFile);
                // Create Video entity
                Video video = new Video();
                video.setSection(section); // Set the section directly
                video.setVideoData(constructFileUrl(videoFilePath)); // Set the file path
                video.setTitle(videoDTO.getTitle());
                video.setDescription(videoDTO.getDescription());
                video.setIsTrial(videoDTO.getIsTrial());
                // Save the Video entity
                return videoRepository.save(video);
            } else {
                throw new IllegalArgumentException("File is not a valid video file");
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
            return null; // Return null if an exception occurs
        }
    }


    @Transactional
    public boolean deleteVideoFromSection(Integer sectionId, Integer videoId) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Optional<Video> optionalVideo = videoRepository.findById(videoId);
                if (optionalVideo.isPresent()) {
                    Video video = optionalVideo.get();
                    videoRepository.delete(video);
                    return true; // Successfully deleted article
                } else {
                    throw new IllegalArgumentException("Video not found for ID: " + videoId);
                }
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle delete failure
            e.printStackTrace();
            return false;
        }
    }
}