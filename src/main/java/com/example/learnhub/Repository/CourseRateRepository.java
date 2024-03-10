package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseRateRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT cr FROM Rating cr WHERE cr.course.courseId = :courseId")
    List<Rating> getCourseRateByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT cr FROM Rating cr WHERE cr.user.userId = :accountId AND cr.course.courseId = :courseId")
    Optional<Rating> getCourseRateByAccountIdAndCourseId(@Param("accountId") Integer accountId, @Param("courseId") Integer courseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Rating cr WHERE cr.ratingId = :id")
    int deleteRatingById(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Rating cr WHERE cr.course.courseId = :courseId")
    int deleteCourseRateByCourseId(@Param("courseId") int courseId);

    @Query("SELECT cr FROM Rating cr WHERE cr.course.courseId = :courseId")
    List<Rating> showCourseRateByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT COUNT(cr.course.courseId) FROM Rating cr WHERE cr.course.courseId = :courseId AND cr.ratingValue > 0")
    Integer countCourseRateByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT CAST(ROUND(AVG(c.ratingValue), 0) AS Double) FROM Rating c WHERE c.course.courseId = ?1 AND c.ratingValue > 0")
    Double avgCourseRateByCourseId(Integer courseId);



    Optional<Rating> findById(int ratingId);

}
