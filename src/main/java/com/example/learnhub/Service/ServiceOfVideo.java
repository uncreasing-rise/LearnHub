package com.example.learnhub.Service;

import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ServiceOfVideo implements IServiceOfVideo {

    private final ServiceOfFile serviceOfFile;
    private final VideoRepository videoRepository;

    @Autowired
    public ServiceOfVideo(ServiceOfFile serviceOfFile, VideoRepository videoRepository) {
        this.serviceOfFile = serviceOfFile;
        this.videoRepository = videoRepository;
    }

    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    public void createVideos(Section section, List<MultipartFile> videoFiles) {
        if (videoFiles != null) {
            for (MultipartFile videoFile : videoFiles) {
                try {
                    // Check if the file is not empty and is a video file
                    if (!videoFile.isEmpty() && isVideoFile(videoFile)) {
                        // Upload video file to GCS using ServiceOfFile
                        serviceOfFile.uploadFile(videoFile);

                        // Create Video entity and associate it with the Section
                        Video video = new Video();
                        video.setVideoScript(""); // Set video script if needed
                        video.setVideoData(""); // Set video data if needed
                        video.setSectionID(section.getSectionId()); // Set the section ID

                        // Save the Video entity
                        createVideo(video);
                    }
                } catch (IOException e) {
                    // Handle upload failure
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isVideoFile(MultipartFile file) {
        // Get the file extension
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = StringUtils.getFilenameExtension(filename);

            // Check if the file extension is for a video file
            // Add more video file extensions as needed (e.g., mkv, flv, etc.)
            return extension != null && (
                    extension.equalsIgnoreCase("mp4") ||
                            extension.equalsIgnoreCase("avi") ||
                            extension.equalsIgnoreCase("mov") ||
                            extension.equalsIgnoreCase("mkv") ||
                            extension.equalsIgnoreCase("flv")
                    // Add more extensions here
            );
        }
        return false; // If the filename is null, consider it as not a video file
    }

}
