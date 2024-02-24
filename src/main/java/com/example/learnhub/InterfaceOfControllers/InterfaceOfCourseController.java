package com.example.learnhub.InterfaceOfControllers;

import com.example.learnhub.DTO.CourseDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/course")
public interface InterfaceOfCourseController {

    @GetMapping("/displayIsNotApprovedCourses")
    List<CourseDTO> displayIsNotApprovedCourses();

    @PostMapping("/showSectionAndVideo")
    ResponseEntity<CourseDTO> showSectionAndVideo(@RequestParam int id) throws IOException;

    @GetMapping("/{language}/{price}")
    List<CourseDTO> findAllCourseByPrice(@PathVariable double price);

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    CourseDTO createCourse(@RequestBody @Valid CourseDTO dto) throws AppServiceExeption, IOException;


    @GetMapping("/getCourses")
    List<CourseDTO> getCourses();

    @GetMapping("/getUnapprovedCourses")
    List<CourseDTO> getUnapprovedCourses();


    @GetMapping("/getCourse/{keyword}")
//search course by keyword
    List<CourseDTO> findCourseThatContainsKeyword(@PathVariable String keyword);

    @GetMapping("/getCourse/category/{category}")
//search course by LIKE category
    List<CourseDTO> getCoursesByCategory(@PathVariable String category);


    @GetMapping("/getCourseSort1")
    List<CourseDTO> getCoursesByPriceHigher();

    @GetMapping("/getCourseSort2")
    List<CourseDTO> getCoursesByPriceLower();

    @GetMapping("/getCourseSort3")
    List<CourseDTO> getCoursesByDateNew();

    @GetMapping("/getCourseSort4")
    List<CourseDTO> getCoursesByDateOld();

    @GetMapping("/{id}")
    ResponseEntity<CourseDTO> getCourseById(@PathVariable int id);

    @PutMapping("/updateCourse/{id}")
    ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody @Valid CourseDTO dto) throws AppServiceExeption, IOException;

    @DeleteMapping("/deleteCourse/{id}")
    void deleteCourse(@PathVariable int id);

    @PostMapping("/rateCourse/{id}/{rating}")
    ResponseEntity<CourseDTO> rateCourse(@PathVariable int id, @PathVariable int rating) throws AppServiceExeption;

}
