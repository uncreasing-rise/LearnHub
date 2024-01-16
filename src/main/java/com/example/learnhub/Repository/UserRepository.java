package com.example.learnhub.Repository;

import com.example.learnhub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRoleId(Integer roleId);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(Integer userId);
    Optional<User> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.image = :imageName WHERE u.userId = :userId")
    int updateImageUser(@Param("userId") int userId, @Param("imageName") String imageName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.token = null WHERE u.userId = :userId")
    int updateToken(@Param("userId") int userId);
}
