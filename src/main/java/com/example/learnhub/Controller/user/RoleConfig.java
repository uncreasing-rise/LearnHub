package com.example.learnhub.Controller.user;


import com.example.learnhub.Entity.Role;
import com.example.learnhub.Repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RoleConfig {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void config() {

            log.info("Goto config");
            List<Role> roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.STUDENT.name());
            if (roles.size() == 0) {
                Role role = new Role(com.example.learnhub.security.Role.STUDENT.name());
                roleRepository.save(role);
            }
            roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.COURSEMANAGER.name());
            if (roles.size() == 0) {
                Role role = new Role(com.example.learnhub.security.Role.COURSEMANAGER.name());
                roleRepository.save(role);
            }
            roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.ADMIN.name());
            if (roles.size() == 0) {
                Role role = new Role(com.example.learnhub.security.Role.ADMIN.name());
                roleRepository.save(role);
            }

    }


}
