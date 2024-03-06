package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    Wishlist findByCourse_CourseId(Integer courseId);
}
