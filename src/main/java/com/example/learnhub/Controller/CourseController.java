package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CourseDTO;
import com.example.learnhub.DTO.ResponeCourseDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Service.ServiceOfCourse;
import com.example.learnhub.Service.ServiceOfLearningDetail;
import com.example.learnhub.Service.ServiceOfSection;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin("*")
@MultipartConfig
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ServiceOfSection serviceOfSection;
    private final ServiceOfCourse serviceOfCourse;
    private final CourseRepository courseRepository;
    private final ServiceOfLearningDetail serviceOfLearningDetail;

    @Autowired
    public CourseController(ServiceOfSection serviceOfSection, ServiceOfCourse courseService, CourseRepository courseRepository, ServiceOfLearningDetail serviceOfLearningDetail) {
        this.serviceOfSection = serviceOfSection;
        this.serviceOfCourse = courseService;
        this.courseRepository = courseRepository;
        this.serviceOfLearningDetail = serviceOfLearningDetail;
    }


    // API để hiển thị thông tin về các phần và video của một khóa học
    @PostMapping("/showSectionAndVideo/{id}")
    public ResponseEntity<ResponeCourseDTO> showSectionAndVideo(@PathVariable int id) {
        try {
            ResponeCourseDTO courseDTO = serviceOfCourse.showSectionAndVideo(id);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // API để tìm kiếm các khóa học theo giá
    @GetMapping("/{price}")
    public List<CourseDTO> findAllCourseByPrice(@PathVariable double price) {
        List<Course> courses = courseRepository.findByPrice(price);
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    // API để thêm mới một khóa học
    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponeCourseDTO createCourse(@RequestPart("courseDTO") @Valid CourseDTO courseDTO,
                                         @RequestPart("articleFiles") List<MultipartFile> articleFiles,
                                         @RequestPart("videoFiles") List<MultipartFile> videoFiles,
                                         @RequestPart("videoTrial") MultipartFile videoTrial,
                                         @RequestPart("ImageFile") MultipartFile imageCourse)
            throws AppServiceExeption, IOException {
        Course course = serviceOfCourse.createCourse(courseDTO, imageCourse, videoTrial);

        List<SectionDTO> sections = courseDTO.getSections();
        for (SectionDTO createSectionDTO : sections) {
            serviceOfSection.createSection(createSectionDTO, course, articleFiles, videoFiles);
        }

        serviceOfLearningDetail.createLearningDetail(courseDTO.getLearningDetail(), course);

        return serviceOfCourse.fromCourseToResponeCourseDTO(course);
    }


    // API để lấy tất cả các khóa học
    @GetMapping
    public ResponseEntity<List<ResponeCourseDTO>> getAllCourses() {
        List<Course> courses = serviceOfCourse.getAllCourses();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }


    // API để tìm kiếm các khóa học theo từ khóa
    @GetMapping("/search")
    public ResponseEntity<List<ResponeCourseDTO>> searchCoursesByKeyword(@RequestParam String keyword) {
        List<Course> courses = serviceOfCourse.findCoursesByKeyword(keyword);
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo danh mục
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = serviceOfCourse.getCoursesByCategory(category);
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo giá cao hơn
    @GetMapping("/price/higher")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByPriceHigher() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceHigher();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo giá thấp hơn
    @GetMapping("/price/lower")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByPriceLower() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceLower();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo ngày mới nhất
    @GetMapping("/date/new")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByDateNew() {
        List<Course> courses = serviceOfCourse.getCoursesByDateNew();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo ngày cũ nhất
    @GetMapping("/date/old")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByDateOld() {
        List<Course> courses = serviceOfCourse.getCoursesByDateOld();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để xóa một khóa học
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        serviceOfCourse.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
