package com.example.learnhub.InterfaceOfControllers;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/video")
public interface InterfaceOfVideoController {

    @PostMapping("/add")
    VideoDTO createNewVideo(String name, MultipartFile data, String script, boolean isTrial, Section section) throws IOException;

    List<VideoDTO> findAllVideoBySectionID();

    VideoDTO findVideoByID();

    List<VideoDTO> findAllVideos();

    @PostMapping("/delete")
    int deleteVideo(int id);
}
