package com.example.learnhub.Service;

import com.example.learnhub.DTO.CourseDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Exceptions.CourseNotFoundException;

import java.util.List;

public interface IServiceOfCourse {
    CourseDTO createCourse(CourseDTO dto) throws AppServiceExeption;

    void createSectionsAndContents(Course course, List<SectionDTO> sections);

    List<CourseDTO> findCoursesByInstructorId(int id) throws CourseNotFoundException;

    List<CourseDTO> getCourseList();

    int updateCourseStatus(int courseId, int status);

    int deleteCourseById(int courseId);
}
