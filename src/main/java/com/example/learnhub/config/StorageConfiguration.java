package com.example.learnhub.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class StorageConfiguration {

    @Bean
    public Storage storage() throws IOException {
        // Load credentials from the JSON file
        String filePath = "learnhub-415909-2647557d5f02.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("Failed to find credentials file: " + filePath);
        }

        // Create credentials from the JSON data
        Credentials credentials = GoogleCredentials.fromStream(inputStream);

        // Build the Storage instance
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    // Replace this variable with your project ID
    private final String projectId = "learnhub-415909";
}
