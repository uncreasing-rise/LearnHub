package com.example.learnhub.Repository;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    Wishlist findByCourse_CourseId(Integer courseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist c WHERE c.course.courseId = :courseId AND c.user.userId = :userId")
    void deleteWishlistItemByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    @Query
    boolean existsByCourse_CourseIdAndUser_UserId(Integer courseId, Integer userId);

    @Query("SELECT c FROM Wishlist c WHERE c.user.userId = :userId")
    List<Wishlist> getAllWishlistItemsByUserId(Integer userId);

    void deleteByCourse_CourseId(int courseid);
}

