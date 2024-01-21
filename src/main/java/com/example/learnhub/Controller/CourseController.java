package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CourseDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Rating;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.InterfaceOfControllers.InterfaceOfCourseController;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Service.ServiceOfCourse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController implements InterfaceOfCourseController {
    @Autowired
    CourseRepository courseRepository;

    private final ServiceOfCourse courseService;

    @Autowired
    public CourseController(ServiceOfCourse courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/displayIsNotApprovedCourses")
    public List<CourseDTO> displayIsNotApprovedCourses() {
        List<Course> unapprovedCourses = courseRepository.findUnapprovedCourses();
        return courseService.fromCourseListToCourseDTOList(unapprovedCourses);
    }

    @PostMapping("/showSectionAndVideo/{id}")
    public ResponseEntity<CourseDTO> showSectionAndVideo(@PathVariable int id) {
        try {
            CourseDTO courseDTO = courseService.showSectionAndVideo(id);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{price}")
    public List<CourseDTO> findAllCourseByPrice(@PathVariable double price) {
        List<Course> courses = courseRepository.findByPrice(price);
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@RequestBody @Valid CourseDTO dto) throws AppServiceExeption {
        try {
            Course createdCourse = courseService.createCourse(dto);
            return courseService.fromCourseToCourseDTO(createdCourse);
        } catch (DataIntegrityViolationException e) {
            throw new AppServiceExeption("Database integrity violation. Fail to create Course");
        } catch (Exception e) {
            throw new AppServiceExeption("Unexpected error. Fail to create Course");
        }
    }


    @GetMapping("/getCourses")
    public List<CourseDTO> getCourses() {
        List<Course> courses = courseService.getAllCourses();
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getUnapprovedCourses")
    public List<CourseDTO> getUnapprovedCourses() {
        List<Course> unapprovedCourses = courseService.getUnapprovedCourses();
        return courseService.fromCourseListToCourseDTOList(unapprovedCourses);
    }

    @GetMapping("/findCourseThatContainsKeyword/{keyword}")
    public List<CourseDTO> findCourseThatContainsKeyword(@PathVariable String keyword) {
        List<Course> courses = courseService.findCoursesByKeyword(keyword);
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByCategory/{category}")
    public List<CourseDTO> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = courseService.getCoursesByCategory(category);
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByPriceHigher")
    public List<CourseDTO> getCoursesByPriceHigher() {
        List<Course> courses = courseService.getCoursesByPriceHigher();
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByPriceLower")
    public List<CourseDTO> getCoursesByPriceLower() {
        List<Course> courses = courseService.getCoursesByPriceLower();
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByDateNew")
    public List<CourseDTO> getCoursesByDateNew() {
        List<Course> courses = courseService.getCoursesByDateNew();
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCoursesByDateOld")
    public List<CourseDTO> getCoursesByDateOld() {
        List<Course> courses = courseService.getCoursesByDateOld();
        return courseService.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/getCourseById/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable int id) {
        CourseDTO courseDTO = courseService.getCourseById(id);

        return courseDTO != null
                ? new ResponseEntity<>(courseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateCourse/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody @Valid CourseDTO dto) {
        CourseDTO updatedCourse = courseService.updateCourse(id, dto);

        return updatedCourse != null
                ? new ResponseEntity<>(updatedCourse, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteCourse/{id}")
    public void deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/rateCourse/{id}/{rating}")
    public ResponseEntity<CourseDTO> rateCourse(@PathVariable int id, @PathVariable int rating) {
        // Create a Rating object with the provided rating value
        Rating newRating = new Rating();
        newRating.setRatingValue(rating);

        // Create a list containing the single rating
        List<Rating> ratings = Collections.singletonList(newRating);

        CourseDTO updatedCourse = courseService.rateCourse(id, ratings);

        return updatedCourse != null
                ? new ResponseEntity<>(updatedCourse, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
