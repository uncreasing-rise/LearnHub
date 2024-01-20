package com.example.learnhub;

import com.example.learnhub.Entity.Role;
import com.example.learnhub.Repository.RoleRepository;
import jakarta.annotation.PostConstruct;
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



}
