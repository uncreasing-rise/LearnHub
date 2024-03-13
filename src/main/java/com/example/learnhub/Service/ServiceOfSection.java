package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Exceptions.CourseNotFoundException;
import com.example.learnhub.Exceptions.SectionNotFoundException;
import com.example.learnhub.Repository.CourseRepository;
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
    private final CourseRepository courseRepository;


    @Autowired
    public ServiceOfSection(SectionRepository sectionRepository, ServiceOfVideo serviceOfVideo, ServiceOfArticle serviceOfArticle, ServiceOfQuiz serviceOfQuiz, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.serviceOfVideo = serviceOfVideo;
        this.serviceOfArticle = serviceOfArticle;
        this.serviceOfQuiz = serviceOfQuiz;
        this.courseRepository = courseRepository;
    }
    public SectionDTO fromSectionToSectionDTO(Section section)
    {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setSectionId(section.getSectionId());
        sectionDTO.setSectionName(section.getSectionName());
        sectionDTO.setCourse(section.getCourse());
        return sectionDTO;
    }
    public ResponeSectionDTO fromSectionToResponeSectionDTO(Section section)
    {
        ResponeSectionDTO sectionDTO = new ResponeSectionDTO();
        sectionDTO.setSectionId(section.getSectionId());
        sectionDTO.setSectionName(section.getSectionName());
        sectionDTO.setQuizzes(section.getQuizzes());
        sectionDTO.setArticles(section.getArticles());
        sectionDTO.setVideos(section.getVideos());
//        sectionDTO.setCourse(section.getCourse());
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

    public Section createSectionToCourse(Integer courseId, SectionDTO sectionDTO, List<MultipartFile> articleFiles, List<MultipartFile> videoFiles) {
        // Tìm khoá học từ cơ sở dữ liệu
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        // Tạo một phần mới cho khoá học
        Section section = new Section();
        section.setSectionName(sectionDTO.getSectionName());
        section.setCourse(course);
        // Lưu phần mới vào cơ sở dữ liệu
        sectionRepository.save(section);
        // Tạo các articles và videos cho phần mới
        List<Article> articles = serviceOfArticle.createArticles(section, articleFiles, sectionDTO.getArticles());
        List<Video> videos = serviceOfVideo.createVideos(section, videoFiles, sectionDTO.getVideos());
        // Tạo quizzes cho phần mới
        List<Quiz> quizzes = serviceOfQuiz.createQuizzes(section, sectionDTO.getQuizzes());
        // Cập nhật phần mới với các articles, videos và quizzes tương ứng
        section.setArticles(articles);
        section.setVideos(videos);
        section.setQuizzes(quizzes);
        // Lưu lại phần đã được cập nhật
        return sectionRepository.save(section);
    }

    public Section updateSection(Integer sectionId, SectionDTO updatedSectionDTO) {
        // Tìm phần cần chỉnh sửa từ cơ sở dữ liệu
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
        // Cập nhật thông tin của phần
        section.setSectionName(updatedSectionDTO.getSectionName());
        // Lưu phần đã cập nhật vào cơ sở dữ liệu
        sectionRepository.save(section);
        return sectionRepository.save(section);
    }
    // Service method to delete a section by ID
    public void deleteSection(Integer sectionId) {
        // Check if the section exists in the database
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));

        // Delete the section from the repository
        sectionRepository.delete(section);
    }



}
