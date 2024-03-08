package com.example.learnhub.Service;

import com.example.learnhub.Entity.Image;
import com.example.learnhub.Repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ServiceOfImage {

    private final ServiceOfFile serviceOfFile;
    private final ImageRepository imageRepository;

    public ServiceOfImage(ServiceOfFile serviceOfFile, ImageRepository imageRepository) {
        this.serviceOfFile = serviceOfFile;
        this.imageRepository = imageRepository;
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Invalid image file provided");
        }

        try {
            if (isImageFile(imageFile)) {
                // Upload image file to storage using ServiceOfFile
                String imageUrl = serviceOfFile.uploadFile(imageFile);

                // Construct the public URL for the image file
                imageUrl = constructFileUrl(imageUrl);

                // Create and save the image entity to the database
                Image image = new Image();
                image.setImageUrl(imageUrl);
                imageRepository.save(image);

                return imageUrl;
            } else {
                throw new IllegalArgumentException("File is not a valid image file");
            }
        } catch (IOException e) {
            // Handle upload failure
            throw new RuntimeException("Failed to upload image file", e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return StringUtils.hasText(filename) && getImageFileExtensions().stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private List<String> getImageFileExtensions() {
        // Add more image file extensions as needed (e.g., jpeg, png, gif, etc.)
        return List.of(".jpg", ".jpeg", ".png");
    }

    public String constructFileUrl(String originalFilename) {
        // Return a public URL for the image file
        return "https://storage.googleapis.com/learnhub_bucket/" + originalFilename;
    }


}
