package com.example.learnhub;

import com.example.learnhub.Entity.Role;
import com.example.learnhub.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LearnHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnHubApplication.class, args);
    }


    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args ->
        {
            List<Role> roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.TRAINER.name());
            if (roles.size() == 0) {
                roleRepository.save(new Role(10, com.example.learnhub.security.Role.TRAINER.name()));
            }
            roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.ADMIN.name());
            if (roles.size() == 0) {
                roleRepository.save(new Role(11, com.example.learnhub.security.Role.ADMIN.name()));
            }
            roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.SUPER_ADMIN.name());
            if (roles.size() == 0) {
                roleRepository.save(new Role(12, com.example.learnhub.security.Role.SUPER_ADMIN.name()));
            }
        };
    }
}
