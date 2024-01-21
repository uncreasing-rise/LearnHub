package com.example.learnhub.Service;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceOfVideo implements IServiceOfVideo {

    private final VideoRepository videoRepository;

    @Autowired
    public ServiceOfVideo(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    public void createVideos(Section section, List<VideoDTO> videoDTOs) {
        if (videoDTOs != null) {
            for (VideoDTO videoDTO : videoDTOs) {
                Video video = new Video();
                video.setVideoScript(videoDTO.getVideoScript());
                video.setVideoData(videoDTO.getVideoData());
                video.setSectionID(video.getSectionID());
                createVideo(video);
            }
        }
    }
}
