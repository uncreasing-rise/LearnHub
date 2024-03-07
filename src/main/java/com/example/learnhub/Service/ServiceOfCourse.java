package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Repository.CourseRateRepository;
import com.example.learnhub.Repository.CourseRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceOfCourse {

    private final CourseRepository courseRepository;
    private final CourseRateRepository courseRateRepository;
    private final ServiceOfSection serviceOfSection;
    private final ServiceOfLearningDetail serviceOfLearningDetail;


    @Autowired
    public ServiceOfCourse(CourseRepository courseRepository, CourseRateRepository courseRateRepository, ServiceOfSection serviceOfSection, ServiceOfLearningDetail serviceOfLearningDetail) {
        this.courseRepository = courseRepository;

        this.courseRateRepository = courseRateRepository;
        this.serviceOfSection = serviceOfSection;
        this.serviceOfLearningDetail = serviceOfLearningDetail;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(CourseDTO courseDTO) {
        // Kiểm tra dữ liệu đầu vào
        if (courseDTO == null) {
            throw new IllegalArgumentException("CourseDTO are required");
        }

        // Tạo Course từ CourseDTO
        Course course = new Course();
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setCourseDes(courseDTO.getCourseDes());
        course.setCoursePrice(courseDTO.getCoursePrice());
        course.setCategory(courseDTO.getCategory());
        course.setIsPassed(courseDTO.getIsPassed());
        course.setCourseDate(courseDTO.getCourseDate());
        course.setLevel(courseDTO.getLevel());
        course.setTag(courseDTO.getTag());
        course.setUserId(courseDTO.getUserId());

        // Lưu Course để có được ID của nó
        return courseRepository.save(course);
    }


    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }



    public List<CourseDTO> fromCourseListToCourseDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::fromCourseToCourseDTO)
                .collect(Collectors.toList());
    }

    public List<Course> getCoursesByDateOld() {
        return courseRepository.findAllByOrderByDateAsc();
    }

    public List<Course> getCoursesByDateNew() {
        return courseRepository.findAllByOrderByDateDesc();
    }

    public List<Course> getCoursesByPriceHigher() {
        return courseRepository.findAllByOrderByPriceDesc();
    }

    public List<Course> getCoursesByPriceLower() {
        return courseRepository.findAllByOrderByPriceAsc();
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> findCoursesByKeyword(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }

    public List<Course> getUnapprovedCourses() {
        return courseRepository.findUnapprovedCourses();
    }



    private List<SectionDTO> mapSectionEntitiesToDTOs(List<Section> sections) {
        return sections.stream()
                .map(this::mapSectionEntityToDTO)
                .collect(Collectors.toList());
    }

    private LearningDetailDTO mapLearningDetailEntityToDTO(LearningDetail learningDetails) {
        if (learningDetails == null) {
            return null;
        }

        LearningDetailDTO learningDetailDTO = new LearningDetailDTO();
        learningDetailDTO.setLearningDetailId(learningDetails.getLearningDetailId());

        if (learningDetails.getCourse() != null) {
            learningDetailDTO.setCourseId(learningDetails.getCourse().getCourseId());
        }

        learningDetailDTO.setBenefit(learningDetails.getBenefit());
        learningDetailDTO.setObjective(learningDetails.getObjective());

        return learningDetailDTO;
    }



    private SectionDTO mapSectionEntityToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setSectionId(section.getSectionId());
        sectionDTO.setSectionName(section.getSectionName());
        sectionDTO.setCourse(section.getCourse());
        return sectionDTO;
    }

    public CourseDTO showSectionAndVideo(@RequestParam int id) {
        Course course = courseRepository.findById(id);
        return  fromCourseToCourseDTO(course);
    }

    public CourseDTO fromCourseToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();

        courseDTO.setCourseID(course.getCourseId());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseDes(course.getCourseDes());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setCategory(course.getCategory());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
        courseDTO.setSections(serviceOfSection.getSectionList(course).stream().map(section -> serviceOfSection.fromSectionToSectionDTO(section)).toList());
        courseDTO.setCountRating(courseRateRepository.countCourseRateByCourseId(course.getCourseId()));
        Double avgRating = courseRateRepository.avgCourseRateByCourseId(course.getCourseId());
        courseDTO.setAvgRating(avgRating != null ? avgRating : 0.0);
//        courseDTO.setAvgRating(0);

        courseDTO.setLearningDetail(serviceOfLearningDetail.getLearningDetailByCourseId(course.getCourseId()));

        courseDTO.setStatus(course.getStatus());

        return courseDTO;
    }

}
