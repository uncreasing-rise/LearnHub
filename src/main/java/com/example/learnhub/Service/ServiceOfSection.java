package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Repository.QuizRepository;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ServiceOfSection {
    private final QuizRepository quỉzRepository;
    private final SectionRepository sectionRepository;
    private final ServiceOfVideo serviceOfVideo;
    private final ServiceOfArticle serviceOfArticle;
    private final ServiceOfQuiz serviceOfQuiz;

    @Autowired
    public ServiceOfSection(QuizRepository quỉzRepository, SectionRepository sectionRepository, ServiceOfVideo serviceOfVideo, ServiceOfArticle serviceOfArticle, ServiceOfQuiz serviceOfQuiz) {
        this.quỉzRepository = quỉzRepository;
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
    public Section createSection(SectionDTO dto, Course course) throws AppServiceExeption {
        Section section = new Section();
        section.setSectionName(dto.getSectionName());
        section.setCourse(course);
        return sectionRepository.save(section);
    }
    @Transactional
    public void createSection(Section section, List<MultipartFile> videoFiles, List<MultipartFile> articleFiles, QuizDTO quizDTO) {
        if (videoFiles == null && articleFiles == null && quizDTO == null) {
            throw new IllegalArgumentException("At least one of video files, article files, or a quiz must be provided.");
        }

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

        // Create quiz if quizDTO is provided
        if (quizDTO != null) {
            // Create quiz associated with this section
            Quiz quiz = serviceOfQuiz.createQuiz(quizDTO);
            // Set the section for the quiz
            quiz.setSection(section);
            // Save the quiz entity
            quỉzRepository.save(quiz);
        }
    }


    public List<Section> getSectionList(Course course) {
        List<Section> sectionList = sectionRepository.findByCourse_CourseId(course.getCourseId());
        return sectionList;
    }

}
