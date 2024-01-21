package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Section;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {

//    List<Section> findByCourseID(Course course);


    @Modifying
    @Transactional
    @Query("delete from Section s where s.course.courseId = ?1")
    int deleteSectionsByCourseID(int CourseID);


}
