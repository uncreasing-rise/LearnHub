package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.CourseRegister;
import com.example.learnhub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRegisterRepository extends JpaRepository<CourseRegister, Integer> {
    List<CourseRegister> findByUser(User user);
    Optional<CourseRegister> findByUserAndCourse(User user, Course course);
}
