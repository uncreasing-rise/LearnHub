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
import java.util.Optional;

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

    public List<Video> updateVideos(Section section, List<MultipartFile> videoFiles, List<VideoDTO> videos) {
        List<Video> currentVideos = section.getVideos();
        List<Video> updatedVideos = new ArrayList<>();

        try {
            for (int i = 0; i < videos.size(); i++) {
                VideoDTO videoDTO = videos.get(i);

                // Check if the video DTO has an ID
                if (videoDTO.getVideoId() != null) {
                    // Find the corresponding video in the current videos list
                    Optional<Video> optionalVideo = currentVideos.stream().filter(v -> v.getVideoId().equals(videoDTO.getVideoId())).findFirst();
                    if (optionalVideo.isPresent()) {
                        Video existingVideo = optionalVideo.get();
                        // Update video information
                        existingVideo.setTitle(videoDTO.getTitle());
                        existingVideo.setDescription(videoDTO.getDescription());
                        existingVideo.setIsTrial(videoDTO.getIsTrial());
                        // Add the existing video to the updated videos list
                        updatedVideos.add(existingVideo);
                    }
                } else {
                    // If the video DTO does not have an ID, it means it's a new video
                    MultipartFile videoFile = videoFiles.get(i);
                    if (isVideoFile(videoFile)) {
                        // Upload video file to GCS using ServiceOfFile
                        String videoFilePath = serviceOfFile.uploadFile(videoFile);
                        // Create Video entity
                        Video newVideo = new Video();
                        newVideo.setSection(section);
                        newVideo.setVideoData(constructFileUrl(videoFilePath));
                        newVideo.setTitle(videoDTO.getTitle());
                        newVideo.setDescription(videoDTO.getDescription());
                        newVideo.setIsTrial(videoDTO.getIsTrial());
                        // Add the new video to the updated videos list
                        updatedVideos.add(videoRepository.save(newVideo));
                    } else {
                        throw new IllegalArgumentException("File is not a valid video file");
                    }
                }
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
        }
        // Return the list of updated and new videos
        return updatedVideos;
    }

}
