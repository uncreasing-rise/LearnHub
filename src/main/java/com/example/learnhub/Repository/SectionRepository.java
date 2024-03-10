package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Section;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {


    @Modifying
    @Transactional
    @Query("delete from Section s where s.course.courseId = ?1")
    int deleteSectionsByCourseID(int CourseID);


    List<Section> findByCourse_CourseId(Integer course);
}
