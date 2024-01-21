package com.example.learnhub.Controller;

import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.InterfaceOfControllers.InterfaceOfVideoController;
import com.example.learnhub.Repository.VideoRepository;
//import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Service.ServiceOfVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VideoController implements InterfaceOfVideoController {
    Path staticPath = Paths.get("static");
    Path videoPath = Paths.get("videos");

    VideoRepository videoRepository;

//    ServiceOfFile serviceOfFile;

    ServiceOfVideo serviceOfVideo;


    public VideoDTO fromVideoIntoResponeVideoDTO(Video video) {

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setVideoId(video.getVideoId());
        videoDTO.setVideoData(video.getVideoData());
        videoDTO.setVideoScript(video.getVideoScript());
        videoDTO.setSectionID(video.getSectionID());
        videoDTO.setIsTrial(video.getIsTrial());

        return videoDTO;
    }
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));



    @Override
    public VideoDTO createNewVideo(String name, MultipartFile data, String script, boolean isTrial, Section section) throws IOException {
        return null;
    }

    @Override
    public List<VideoDTO> findAllVideoBySectionID() {
        return null;
    }

    @Override
    public VideoDTO findVideoByID() {
        return null;
    }

    @Override
    public List<VideoDTO> findAllVideos() {
        return null;
    }

    @Override
    public int deleteVideo(int id) {
        //moi sua
        int delete = videoRepository.deleteVideo(id);
        return delete;
    }
}
