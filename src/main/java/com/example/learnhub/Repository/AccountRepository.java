//package com.example.learnhub.Repository;
//
//import com.example.learnhub.Entity.User;
//import com.example.learnhub.Entity.Role; // Import the Role entity
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface AccountRepository extends JpaRepository<User, Integer> {
//
//    boolean existsByUsername(String username); // Use existsByUsername instead of existByUsername
//
//    List<User> findByRole(Role role); // Corrected method name and parameter type
//
//    List<User> findByUserNameContaining(String username);
//
//    Optional<User> findById(int userId); // Corrected method name
//
//    Optional<User> findByUserName(String userName);
//
//    @Modifying
//    @Query("UPDATE User u SET u.userPassword = :password WHERE u.email = :email")
//    int updatePassword(String email, String password); // Provide a valid query string
//
//}
