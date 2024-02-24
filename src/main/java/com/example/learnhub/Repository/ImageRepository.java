package com.example.learnhub.Repository;


import com.example.learnhub.Entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {



    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.course.courseId = ?1")
    int deleteImageByCourseId(Integer courseId);

    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.imageUrl = ?1 WHERE i.course.courseId = ?2")
    void updateImages(String imageUrl, Integer courseId);

    boolean existsByCourse_CourseId(int courseId);

    Image findByCourse_CourseId(int courseId);
}
