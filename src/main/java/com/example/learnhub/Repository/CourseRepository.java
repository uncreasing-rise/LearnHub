package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.courseTitle LIKE CONCAT('%', ?1, '%') OR c.courseDes LIKE CONCAT('%', ?1, '%') ORDER BY \n" +
            "CASE \n" +
            "    WHEN c.courseTitle LIKE CONCAT(?1, '%') THEN 1 \n" +
            "    WHEN c.courseDes LIKE CONCAT(?1, '%') THEN 2 \n" +
            "    ELSE 3 \n" +
            "END ")
    List<Course> findCourseThatContainsKeyword(String keyword);

    @Query("SELECT c FROM Course c WHERE c.coursePrice <= ?1")
    List<Course> findByPrice(Double price);

    @Query("SELECT SUM(c.coursePrice) FROM Course c GROUP BY c.userId HAVING c.userId = ?1")
    Double findSumCoursesPrice(int id);

    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.images = ?2 WHERE c.courseId = ?1")
    int updateMainImage(int courseId, String imageName);

    @Query("SELECT c FROM Course c ORDER BY c.coursePrice DESC")
    List<Course> findAllByOrderByPriceDesc();

    @Query("SELECT c FROM Course c ORDER BY c.coursePrice ASC")
    List<Course> findAllByOrderByPriceAsc();

    @Query("SELECT c FROM Course c ORDER BY c.courseDate DESC")
    List<Course> findAllByOrderByDateDesc();

    @Query("SELECT c FROM Course c ORDER BY c.courseDate ASC")
    List<Course> findAllByOrderByDateAsc();

    @Modifying
    @Transactional
    @Query("DELETE FROM Course c WHERE c.courseId = ?1")
    int deleteViolatedCourse(int courseId);


    Course findById(int courseId);

    @Query("SELECT c FROM Course c WHERE c.status = 1")
    List<Course> displayIsNotApprovedCourses();

    @Query("SELECT c FROM Course c WHERE c.categoryId = :categoryId")
    List<Course> findByCategory(@Param("categoryId") String categoryId);

    ;

    @Query("SELECT c FROM Course c WHERE c.status <> 2")
    List<Course> findUnapprovedCourses();

    @Query("SELECT c FROM Course c WHERE c.courseTitle LIKE %:keyword% OR c.courseDes LIKE %:keyword%")
    List<Course> findByKeyword(@Param("keyword") String keyword);

}
