package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ServiceOfSection {
    private final SectionRepository sectionRepository;
    private final ServiceOfVideo serviceOfVideo;
    private final ServiceOfArticle serviceOfArticle;
    private final ServiceOfQuiz serviceOfQuiz;


    @Autowired
    public ServiceOfSection(SectionRepository sectionRepository, ServiceOfVideo serviceOfVideo, ServiceOfArticle serviceOfArticle, ServiceOfQuiz serviceOfQuiz) {
        this.sectionRepository = sectionRepository;
        this.serviceOfVideo = serviceOfVideo;
        this.serviceOfArticle = serviceOfArticle;
        this.serviceOfQuiz = serviceOfQuiz;
    }
    public SectionDTO fromSectionToSectionDTO(Section section)
    {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setSectionId(section.getSectionId());
        sectionDTO.setSectionName(section.getSectionName());
        sectionDTO.setCourse(section.getCourse());
        return sectionDTO;
    }
    public Section createSection(SectionDTO dto, Course course, List<MultipartFile> articleFiles, List<MultipartFile> videoFiles) throws AppServiceExeption, IOException {
        Section section = new Section();
        section.setSectionName(dto.getSectionName());
        section.setCourse(course);
        sectionRepository.save(section);

        // Create articles associated with the section
        List<Article> articles = serviceOfArticle.createArticles(section, articleFiles, dto.getArticles());


        // Create videos associated with the section
        List<Video> videos = serviceOfVideo.createVideos(section, videoFiles, dto.getVideos());

        // Create quiz asscociated with the section
        List<Quiz> quizzes = serviceOfQuiz.createQuizzes(section, dto.getQuizzes());

        // Set created articles and videos for the section
        section.setArticles(articles);
        section.setVideos(videos);
        section.setQuizzes(quizzes);

        return sectionRepository.save(section);
    }


    public List<Section> getSectionList(Course course) {
        return sectionRepository.findByCourse_CourseId(course.getCourseId());
    }


}
