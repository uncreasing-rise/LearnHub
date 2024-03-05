package com.example.learnhub.Repository;

import com.example.learnhub.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartItemId(Long cartItemId);
    List<CartItem> findByCartCartId(Integer cartId);
    List<CartItem> findByCartCartIdAndCourseCourseId(Integer cartId, Integer courseId);


}
