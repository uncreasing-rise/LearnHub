package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.DTO.VideoDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ServiceOfSection {

    private final SectionRepository sectionRepository;
    private final ServiceOfVideo serviceOfVideo;
    private final ServiceOfArticle serviceOfArticle;

    @Autowired
    public ServiceOfSection(SectionRepository sectionRepository, ServiceOfVideo serviceOfVideo, ServiceOfArticle serviceOfArticle) {
        this.sectionRepository = sectionRepository;
        this.serviceOfVideo = serviceOfVideo;
        this.serviceOfArticle = serviceOfArticle;
    }

    @Transactional
    public void createSection(Section section, List<MultipartFile> videoFiles, List<MultipartFile> articleFiles) {
        if (videoFiles != null && !videoFiles.isEmpty()) {
            serviceOfVideo.createVideos(section, videoFiles);
        }

        if (articleFiles != null && !articleFiles.isEmpty()) {
            serviceOfArticle.createArticles(section, articleFiles);
        }

        // Set the courseId for the section
        section.setCourse(section.getCourse());
        // Save the section entity
        sectionRepository.save(section);
    }



}
