package com.example.learnhub.Service;

import com.example.learnhub.Entity.Course;

import java.util.List;

public interface IServiceOfCourse {
    List<Course> getAllCourses();

    Course getCourseById(int courseId);

    Course createCourse(Course course);

    Course updateCourse(int courseId, Course updatedCourse);

    void deleteCourse(int courseId);

}
