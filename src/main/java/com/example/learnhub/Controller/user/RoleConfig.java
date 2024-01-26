package com.example.learnhub.Controller.user;


import com.example.learnhub.Entity.Role;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.RoleRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.security.utils.AESUtils;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private UserRepository userRepository;


    @Value("${aes.key}")
    private String key;

    @PostConstruct
    @SneakyThrows
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

        User user = userRepository.findByEmail("Admin@email.com").orElse(
            new User()
            .setEnable(true)
            .setEmail("Admin@email.com")
                .setFacebook("facebook")
                .setToken("token")
                .setImage("url")
                .setFullName("fullname")
            .setRoleId(roleRepository.findByRoleName(com.example.learnhub.security.Role.ADMIN.name()).get(0).getRoleId())
            .setUserPassword(AESUtils.encrypt("Password123@", key)));
        userRepository.save(user);

    }


}
