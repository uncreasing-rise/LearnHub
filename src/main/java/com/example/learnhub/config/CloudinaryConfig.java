package com.example.learnhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        String CLOUD_NAME = "deybjiyuq";
        config.put("cloud_name", CLOUD_NAME);
        String API_KEY = "627834431625233";
        config.put("api_key", API_KEY);
        String API_SECRET = "_5mYR0Mm1jkDt-igdZdLlEk5Ch8";
        config.put("api_secret", API_SECRET);

        // Specify the platform adapter based on your environment
        config.put("platform_adapter", "com.cloudinary.http44.UploaderStrategy");

        return new Cloudinary(config);
    }
}