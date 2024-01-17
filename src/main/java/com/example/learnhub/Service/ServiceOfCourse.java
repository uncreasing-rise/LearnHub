package com.example.learnhub.Service;

import com.example.learnhub.Exceptions.CourseNotFoundException;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceOfCourse {

    private final CourseRepository courseRepository;

    @Autowired
    public ServiceOfCourse(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(int courseId, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(courseId);

        if (existingCourse == null) {
            throw new CourseNotFoundException(courseId);
        }

        if (updatedCourse.getCourseTitle() != null) {
            existingCourse.setCourseTitle(updatedCourse.getCourseTitle());
        }

        if (updatedCourse.getCourseDes() != null) {
            existingCourse.setCourseDes(updatedCourse.getCourseDes());
        }

        if (updatedCourse.getCoursePrice() != null) {
            existingCourse.setCoursePrice(updatedCourse.getCoursePrice());
        }
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }
}
