package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CourseDTO;
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

import java.util.*;
@MultipartConfig
@RestController
@RequestMapping("/courses")
public class CourseController{
private final ServiceOfSection   serviceOfSection;
    private final ServiceOfCourse serviceOfCourse;
    private final CourseRepository courseRepository;
    private final ServiceOfLearningDetail serviceOfLearningDetail;

    @Autowired
    public CourseController(ServiceOfSection serviceOfSection,  ServiceOfCourse courseService, CourseRepository courseRepository, ServiceOfLearningDetail serviceOfLearningDetail) {
        this.serviceOfSection = serviceOfSection;
        this.serviceOfCourse = courseService;
        this.courseRepository = courseRepository;
        this.serviceOfLearningDetail = serviceOfLearningDetail;
    }

    @GetMapping("/displayIsNotApprovedCourses")
    public List<CourseDTO> displayIsNotApprovedCourses() {
        List<Course> unapprovedCourses = courseRepository.findUnapprovedCourses();
        return serviceOfCourse.fromCourseListToCourseDTOList(unapprovedCourses);
    }

    @PostMapping("/showSectionAndVideo/{id}")
    public ResponseEntity<CourseDTO> showSectionAndVideo(@PathVariable int id) {
        try {
            CourseDTO courseDTO = serviceOfCourse.showSectionAndVideo(id);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{price}")
    public List<CourseDTO> findAllCourseByPrice(@PathVariable double price) {
        List<Course> courses = courseRepository.findByPrice(price);
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@RequestBody @Valid CourseDTO courseDTO)
                                  throws AppServiceExeption {
        Course course = serviceOfCourse.createCourse(courseDTO);

        List<SectionDTO> sections = courseDTO.getSections();
        for (SectionDTO createSectionDTO: sections) {
            serviceOfSection.createSection(createSectionDTO, course);
        }
        serviceOfLearningDetail.createLearningDetail(courseDTO.getLearningDetail(),course);

        return serviceOfCourse.fromCourseToCourseDTO(course);
    }





    @GetMapping("/getCourses")
    public List<CourseDTO> getCourses() {
        List<Course> courses = serviceOfCourse.getAllCourses();
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getUnapprovedCourses")
    public List<CourseDTO> getUnapprovedCourses() {
        List<Course> unapprovedCourses = serviceOfCourse.getUnapprovedCourses();
        return serviceOfCourse.fromCourseListToCourseDTOList(unapprovedCourses);
    }

    @GetMapping("/findCourseThatContainsKeyword/{keyword}")
    public List<CourseDTO> findCourseThatContainsKeyword(@PathVariable String keyword) {
        List<Course> courses = serviceOfCourse.findCoursesByKeyword(keyword);
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByCategory/{category}")
    public List<CourseDTO> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = serviceOfCourse.getCoursesByCategory(category);
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByPriceHigher")
    public List<CourseDTO> getCoursesByPriceHigher() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceHigher();
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByPriceLower")
    public List<CourseDTO> getCoursesByPriceLower() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceLower();
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByDateNew")
    public List<CourseDTO> getCoursesByDateNew() {
        List<Course> courses = serviceOfCourse.getCoursesByDateNew();
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByDateOld")
    public List<CourseDTO> getCoursesByDateOld() {
        List<Course> courses = serviceOfCourse.getCoursesByDateOld();
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }




    @DeleteMapping("/deleteCourse/{id}")
    public void deleteCourse(@PathVariable int id) {
        serviceOfCourse.deleteCourse(id);
    }

}
