package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.LearningDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningDetailRepository extends JpaRepository<LearningDetail,Integer> {
    LearningDetail findByCourse_CourseId(int course);

    void deleteByCourse_CourseId(int courseId);
}
