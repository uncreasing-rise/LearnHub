package com.example.learnhub.Service;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;

import java.util.List;

public interface IServiceOfVideo {
    Video createVideo(Video video);

    void createVideos(Section section, List<VideoDTO> videoDTOs);
}
