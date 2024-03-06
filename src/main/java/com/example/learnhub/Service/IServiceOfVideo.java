package com.example.learnhub.Service;

import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IServiceOfVideo {
    Video createVideo(Video video);

    Optional<Video> findVideoById(Integer id);

    Video updateVideo(Integer id, Video updatedVideo);

    void deleteVideo(Integer id);

    void createVideos(Section section, List<MultipartFile> videoFiles);
}
