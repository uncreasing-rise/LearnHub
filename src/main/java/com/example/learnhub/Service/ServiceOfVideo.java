package com.example.learnhub.Service;

import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
                    // Upload video file to GCS using ServiceOfFile
                    serviceOfFile.uploadFile(videoFile);

                    // Create Video entity and associate it with the Section
                    Video video = new Video();
                    video.setVideoScript(""); // Set video script if needed
                    video.setVideoData(""); // Set video data if needed
                    video.setSectionID(section.getSectionId()); // Set the section ID

                    // Save the Video entity
                    createVideo(video);
                } catch (IOException e) {
                    // Handle upload failure
                    e.printStackTrace();
                }
            }
        }
    }
}
