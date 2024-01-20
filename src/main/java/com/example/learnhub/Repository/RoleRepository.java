package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByRoleName(String name);
}
