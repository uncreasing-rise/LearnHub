package com.example.learnhub.Repository;

import com.example.learnhub.Entity.LearningDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningDetailRepository extends JpaRepository<LearningDetail,Integer> {
    LearningDetail findByCourse_CourseId(int course);

}
