package com.example.learnhub.Repository;


import com.example.learnhub.Entity.User;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String phoneNumber);
    //SELECT * FROM users WHERE email=?
}

