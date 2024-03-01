package com.example.learnhub.config;


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

import org.springframework.stereotype.Component;

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
        try {
            log.info("Go to config");
            createRoleIfNotExists(com.example.learnhub.security.Role.STUDENT.name());
            createRoleIfNotExists(com.example.learnhub.security.Role.COURSEMANAGER.name());
            createRoleIfNotExists(com.example.learnhub.security.Role.ADMIN.name());
        } catch (Exception e) {
            log.error("Error during RoleConfig initialization", e);
        }
    }

    private void createRoleIfNotExists(String roleName) throws Exception {
        List<Role> roles = roleRepository.findByRoleName(roleName);
        if (roles.isEmpty()) {
            Role role = new Role(roleName);
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
