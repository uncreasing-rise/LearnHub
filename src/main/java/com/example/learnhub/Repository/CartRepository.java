package com.example.learnhub.Repository;


import com.example.learnhub.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCartId(Long cartId);
}
