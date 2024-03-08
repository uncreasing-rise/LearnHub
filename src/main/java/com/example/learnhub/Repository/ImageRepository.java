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
    @Query("DELETE FROM Image i WHERE i. = ?1")
    int deleteImageByCourseId(Integer courseId);


}
