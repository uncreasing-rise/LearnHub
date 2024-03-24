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
    @Query("delete from Answer a where a.question.id in " +
            "(select q.id from Question q where q.quiz.section.sectionId in " +
            "(select s.sectionId from Section s where s.course.courseId = ?1))")
    void deleteAnswersByCourseId(int courseId);


    @Modifying
    @Transactional
    @Query("delete from Question q where q.quiz.section.sectionId in (select s.sectionId from Section s where s.course.courseId = ?1)")
    void deleteQuestionsByCourseId(int courseId);

    @Modifying
    @Transactional
    @Query("delete from Article q where q.section.sectionId in (select s.sectionId from Section s where s.course.courseId = ?1)")
    void deleteArticlesByCourseId(int courseId);

    @Modifying
    @Transactional
    @Query("delete from Video q where q.section.sectionId in (select s.sectionId from Section s where s.course.courseId = ?1)")
    void deleteVideosByCourseId(int courseId);

    @Modifying
    @Transactional
    @Query("delete from Quiz q where q.section.sectionId in (select s.sectionId from Section s where s.course.courseId = ?1)")
    void deleteQuizzesByCourseId(int courseId);


    @Modifying
    @Transactional
    @Query("delete from Section s where s.course.courseId = ?1")
    void deleteSectionsByCourseID(int courseId);



    List<Section> findByCourse_CourseId(Integer course);

}
