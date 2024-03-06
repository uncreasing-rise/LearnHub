package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    public Course createCourse(CourseDTO courseDTO, List<MultipartFile> videoFiles, List<MultipartFile> articleFiles) {
        // Kiểm tra dữ liệu đầu vào
        if (courseDTO == null || videoFiles == null || articleFiles == null) {
            throw new IllegalArgumentException("CourseDTO and file lists are required");
        }
        // Tạo Course từ CourseDTO
        Course course = new Course();
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setCourseDes(courseDTO.getCourseDes());
        course.setCoursePrice(courseDTO.getCoursePrice());
        course.setCategory(courseDTO.getCategory());
        course.setIsPassed(courseDTO.getIsPassed());
        course.setCourseDate(courseDTO.getCourseDate());

        // Tạo một đối tượng Rating mới và đặt ID của nó
        Rating rating = new Rating();
        rating.setRatingId(courseDTO.getRatingId());

        // Thêm đối tượng Rating mới vào danh sách các đánh giá
        course.getRatings().add(rating);

        course.setLevel(courseDTO.getLevel());
        course.setTag(courseDTO.getTag());
        course.setUserId(courseDTO.getUserId());

        // Lưu Course để có được ID của nó
        Course savedCourse = courseRepository.save(course);

        // Tạo và liên kết các Section với Course đã lưu
        createSections(savedCourse, courseDTO.getSections());

        // Xử lý và lưu các tệp video và bài báo ở đây

        return courseRepository.save(course);
    }


    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }

    private void createSections(Course course, List<SectionDTO> sectionDTOs) {
        if (sectionDTOs != null) { // Kiểm tra xem danh sách section có tồn tại không
            for (SectionDTO sectionDTO : sectionDTOs) {
                if (sectionDTO != null) { // Kiểm tra xem mỗi sectionDTO có tồn tại không
                    Section section = new Section();
                    section.setSectionName(sectionDTO.getSectionName());
                    section.setCourse(course); // Thiết lập courseId cho section

                    // Lưu section vào cơ sở dữ liệu và nhận lại section đã lưu
                    Section savedSection = sectionRepository.save(section);

                    // Gọi phương thức createSection trong serviceOfSection để xử lý tệp tin và lưu chúng vào cơ sở dữ liệu
                    serviceOfSection.createSection(savedSection, sectionDTO.getVideoFiles(), sectionDTO.getArticleFiles());
                }
            }
        }
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
        courseDTO.setCategory(course.getCategory());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setRatingId(course.getRatings().isEmpty() ? null : course.getRatings().get(0).getRatingId());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
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
        courseDTO.setCategory(course.getCategory());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setRatingId(course.getRatings().isEmpty() ? null : course.getRatings().get(0).getRatingId());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
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
