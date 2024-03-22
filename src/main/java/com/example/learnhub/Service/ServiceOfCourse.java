package com.example.learnhub.Service;

import com.example.learnhub.DTO.*;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.CourseNotFoundException;
import com.example.learnhub.Repository.CourseRateRepository;
import com.example.learnhub.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOfCourse {

    private final CourseRepository courseRepository;
    private final CourseRateRepository courseRateRepository;
    private final ServiceOfSection serviceOfSection;
    private final ServiceOfLearningDetail serviceOfLearningDetail;
    private final ServiceOfFile serviceOfFile;

    @Autowired
    public ServiceOfCourse(CourseRepository courseRepository, CourseRateRepository courseRateRepository, ServiceOfSection serviceOfSection, ServiceOfLearningDetail serviceOfLearningDetail, ServiceOfFile serviceOfFile) {
        this.courseRepository = courseRepository;

        this.courseRateRepository = courseRateRepository;
        this.serviceOfSection = serviceOfSection;
        this.serviceOfLearningDetail = serviceOfLearningDetail;
        this.serviceOfFile = serviceOfFile;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(CourseDTO courseDTO, MultipartFile image, MultipartFile video) throws IOException {
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
        course.setImageUrl(serviceOfFile.uploadImage(image));
        course.setVideoUrl(serviceOfFile.uploadVideo(video));
        // Lưu Course để có được ID của nó
        return courseRepository.save(course);
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }

    public List<CourseDTO> fromCourseListToCourseDTOList(List<Course> courses) {
        return courses.stream().map(this::fromCourseToCourseDTO).collect(Collectors.toList());
    }
    public List<Course> getCoursesByPriceRange(double minPrice, double maxPrice) {
        return courseRepository.findCoursesByPriceRange(minPrice, maxPrice);
    }

    public List<Course> getCoursesByDateOld() {
        return courseRepository.findAllByOrderByDateAsc();
    }

    public List<Course> getCoursesByDateNew() {
        return courseRepository.findAllByOrderByDateDesc();
    }
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> findCoursesByKeyword(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }
    public List<Course> getCoursesByPriceHigher() {
        return courseRepository.findAllByOrderByPriceDesc();
    }

    public List<Course> getCoursesByPriceLower() {
        return courseRepository.findAllByOrderByPriceAsc();
    }



    public ResponeCourseDTO showSectionAndVideo(@RequestParam int id) {
        Course course = courseRepository.findById(id);
        return fromCourseToResponeCourseDTO(course);
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
        courseDTO.setSections(serviceOfSection.getSectionList(course).stream().map(serviceOfSection::fromSectionToSectionDTO).toList());
        courseDTO.setCountRating(courseRateRepository.countCourseRateByCourseId(course.getCourseId()));
        Double avgRating = courseRateRepository.avgCourseRateByCourseId(course.getCourseId());
        courseDTO.setAvgRating(avgRating != null ? avgRating : 0.0);
        courseDTO.setLearningDetail(serviceOfLearningDetail.getLearningDetailByCourseId(course.getCourseId()));
        courseDTO.setStatus(course.getStatus());
        return courseDTO;
    }

    public ResponeCourseDTO fromCourseToResponeCourseDTO(Course course)  {
        ResponeCourseDTO courseDTO = new ResponeCourseDTO();
        courseDTO.setCourseID(course.getCourseId());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseDes(course.getCourseDes());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setCategoryName(course.getCategory().getCategoryName());
        courseDTO.setIsPassed(course.getIsPassed());
        courseDTO.setCourseDate(course.getCourseDate());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setTag(course.getTag());
        courseDTO.setUserId(course.getUserId());
        courseDTO.setImage(course.getImageUrl());
        courseDTO.setVideoTrial(course.getVideoUrl());
        courseDTO.setSections(serviceOfSection.getSectionList(course).stream().map(serviceOfSection::fromSectionToResponeSectionDTO).toList());
        courseDTO.setCountRating(courseRateRepository.countCourseRateByCourseId(course.getCourseId()));
        Double avgRating = courseRateRepository.avgCourseRateByCourseId(course.getCourseId());
        courseDTO.setAvgRating(avgRating != null ? avgRating : 0.0);
        courseDTO.setLearningDetail(serviceOfLearningDetail.getLearningDetailByCourseId(course.getCourseId()));
        courseDTO.setStatus(course.getStatus());
        return courseDTO;
    }
    public ResponeCourseDTO fromCourseToListResponeCourseDTO(Course course) {
        ResponeCourseDTO courseDTO = new ResponeCourseDTO();
        courseDTO.setCourseID(course.getCourseId());
        courseDTO.setImage(course.getImageUrl());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setCategoryName(course.getCategory().getCategoryName());
        Double avgRating = courseRateRepository.avgCourseRateByCourseId(course.getCourseId());
        courseDTO.setAvgRating(avgRating != null ? avgRating : 0.0);
        return courseDTO;
    }

    public ResponeCourseDTO updateCourse(Integer courseId, CourseDTO updatedCourseDTO) throws CourseNotFoundException {
        // Retrieve the existing course from the database
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        // Update the existing course with the new data
        existingCourse.setCourseTitle(updatedCourseDTO.getCourseTitle());
        existingCourse.setCourseDes(updatedCourseDTO.getCourseDes());
        existingCourse.setCoursePrice(updatedCourseDTO.getCoursePrice());
        existingCourse.setCategory(updatedCourseDTO.getCategory());
        existingCourse.setIsPassed(updatedCourseDTO.getIsPassed());
        existingCourse.setCourseDate(updatedCourseDTO.getCourseDate());
        existingCourse.setLevel(updatedCourseDTO.getLevel());
        existingCourse.setTag(updatedCourseDTO.getTag());
        existingCourse.setUserId(updatedCourseDTO.getUserId());
        existingCourse.setStatus(updatedCourseDTO.getStatus());
        // Update sections if needed
        // Save the updated course to the database
        Course updatedCourse = courseRepository.save(existingCourse);
        // Convert the updated course to ResponseCourseDTO and return
        return fromCourseToResponeCourseDTO(updatedCourse);
    }
}

