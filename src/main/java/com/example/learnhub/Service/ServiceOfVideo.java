package com.example.learnhub.Service;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ServiceOfVideo implements IServiceOfVideo {


    private final ServiceOfFile serviceOfFile;
    private final VideoRepository videoRepository;

    public ServiceOfVideo(ServiceOfFile serviceOfFile, VideoRepository videoRepository) {
        this.serviceOfFile = serviceOfFile;
        this.videoRepository = videoRepository;
    }


    @Override
    public Video saveVideo(VideoDTO dto) {
        Video video = new Video();
        video.setTitle(dto.getTitle());
        video.setSection(dto.getSection());
        video.setVideoData(dto.getVideoUrl());
        video.setDescription(dto.getDescription());
        video.setIsTrial(dto.getIsTrial());
        return videoRepository.save(video) ;
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

    public VideoDTO fromVideoIntoResponeVideoDTO(Video video) {

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setTitle(video.getTitle());
        videoDTO.setVideoUrl(video.getVideoData());
        videoDTO.setDescription(video.getDescription());
        videoDTO.setIsTrial(video.getIsTrial());

        return videoDTO;
    }
    @Override
    public List<Video> getVideos() {

        return null;
    }
}
