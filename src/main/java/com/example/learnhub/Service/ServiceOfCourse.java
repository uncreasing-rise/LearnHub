package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.CourseNotFoundException;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceOfCourse {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final ServiceOfSection serviceOfSection;

    @Autowired
    public ServiceOfCourse(CourseRepository courseRepository, SectionRepository sectionRepository, ServiceOfSection serviceOfSection) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.serviceOfSection = serviceOfSection;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setCourseDes(courseDTO.getCourseDes());
        course.setCoursePrice(courseDTO.getCoursePrice());
        course.setCategoryId(courseDTO.getCategoryId());
        course.setIsPassed(courseDTO.getIsPassed());
        course.setCourseDate(courseDTO.getCourseDate());
        course.setRatings(course.getRatings());
        course.setLevel(courseDTO.getLevel());
        course.setTag(courseDTO.getTag());
        course.setUserId(courseDTO.getUserId());

        Course savedCourse = courseRepository.save(course);

        createSections(savedCourse, courseDTO.getSections());

        return savedCourse;
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }

    private void createSections(Course course, List<SectionDTO> sectionDTOs) {
        for (SectionDTO sectionDTO : sectionDTOs) {
            Section section = new Section();
            section.setSectionName(sectionDTO.getSectionName());
            section.setCourse(course);

            Section savedSection = sectionRepository.save(section);

            serviceOfSection.createVideos(savedSection, sectionDTO.getVideoFiles());
            serviceOfSection.createArticles(savedSection, sectionDTO.getArticles());
        }
    }

    private void updateSections(Course course, List<SectionDTO> sectionDTOs) {
        course.getSections().clear();
        Course updatedCourse = courseRepository.save(course);
        createSections(updatedCourse, sectionDTOs);
    }

    public List<CourseDTO> fromCourseListToCourseDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::fromCourseToCourseDTO)
                .collect(Collectors.toList());
    }
    public CourseDTO fromCourseToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseID(course.getCourseId());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseDes(course.getCourseDes());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setCategoryId(course.getCategoryId());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setRatingId(course.getRatings().isEmpty() ? null : course.getRatings().get(0).getRatingId());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
        courseDTO.setCategory(mapCategoryEntityToDTO(course.getCategory()));
        courseDTO.setSections(mapSectionEntitiesToDTOs(course.getSections()));
        courseDTO.setLearningDetail(mapLearningDetailEntityToDTO(course.getLearningDetail()));
        courseDTO.setStatus(course.getStatus());

        return courseDTO;
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

    public CourseDTO getCourseById(int id) {
        Course course = courseRepository.findById(id);
        return course != null ? mapCourseToDTO(course) : null;
    }

    public CourseDTO updateCourse(int id, CourseDTO dto) {
        Course existingCourse = courseRepository.findById(id);

        if (existingCourse != null) {
            existingCourse.setCourseTitle(dto.getCourseTitle());
            existingCourse.setCourseDes(dto.getCourseDes());

            Course updatedCourse = courseRepository.save(existingCourse);

            return mapCourseToDTO(updatedCourse);
        } else {
            return null;
        }
    }

    public CourseDTO rateCourse(int courseId, List<Rating> newRatings) {
        Optional<Course> optionalCourse = Optional.ofNullable(courseRepository.findById(courseId));

        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            // Set the new list of ratings to the existing course
            existingCourse.setRatings(newRatings);

            Course updatedCourse = courseRepository.save(existingCourse);

            return mapCourseToDTO(updatedCourse);
        } else {
            return null; // Handle the case when the course is not found
        }
    }


    private CourseDTO mapCourseToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseDes(course.getCourseDes());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setCategoryId(course.getCategoryId());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setRatingId(course.getRatings().isEmpty() ? null : course.getRatings().get(0).getRatingId());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
        courseDTO.setCategory(mapCategoryEntityToDTO(course.getCategory()));
        courseDTO.setSections(mapSectionEntitiesToDTOs(course.getSections()));
        courseDTO.setLearningDetail(mapLearningDetailEntityToDTO(course.getLearningDetail()));
        courseDTO.setStatus(course.getStatus());

        return courseDTO;
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

    private CategoryDTO mapCategoryEntityToDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }

    private SectionDTO mapSectionEntityToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setSectionId(section.getSectionId());
        sectionDTO.setSectionName(section.getSectionName());
        sectionDTO.setCourseId(section.getCourse().getCourseId());
        return sectionDTO;
    }

    public CourseDTO showSectionAndVideo(@RequestParam int id) {
        Course course = courseRepository.findById(id);
        return  fromCourseToCourseDTO(course);
    }

}
