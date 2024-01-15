package com.example.learnhub.Repository;

import com.example.learnhub.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<User, Integer> {
    public boolean existByUsername(String username);

    List<User> findByRoleID(String roleID);
    List<User> findByUserNameContaining(String username);
    Optional<User> findByID(int userID);
    Optional<User> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query()
    int updatePassword(String email, String password);
}
