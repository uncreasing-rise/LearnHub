package com.example.learnhub.Service;

import com.example.learnhub.DTO.LearningDetailDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.LearningDetail;
import com.example.learnhub.Repository.LearningDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service

public class ServiceOfLearningDetail {
    private final LearningDetailRepository learningDetailRepository;

    public ServiceOfLearningDetail(LearningDetailRepository learningDetailRepository) {
        this.learningDetailRepository = learningDetailRepository;
    }

    @Transactional
    public LearningDetail createLearningDetail(LearningDetailDTO learningDetailDTO, Course course) {
        LearningDetail learningDetail = new LearningDetail();
        learningDetail.setBenefit(learningDetailDTO.getBenefit());
        learningDetail.setObjective(learningDetailDTO.getObjective());
        learningDetail.setCourse(course); // Associate the learning detail with the course
        // Save the learning detail to the database
        learningDetailRepository.save(learningDetail);
    return learningDetailRepository.save(learningDetail);
    }

    public LearningDetailDTO getLearningDetailByCourseId(Integer courseId) {
        LearningDetail learningDetail = learningDetailRepository.findByCourse_CourseId(courseId);
        LearningDetailDTO learningDetailDTO = new LearningDetailDTO();
        if (learningDetail != null) {
            // Populate LearningDetailDTO with data from LearningDetail entity
            learningDetailDTO.setBenefit(learningDetail.getBenefit());
            learningDetailDTO.setObjective(learningDetail.getObjective());
        }
        return learningDetailDTO;
    }
}
