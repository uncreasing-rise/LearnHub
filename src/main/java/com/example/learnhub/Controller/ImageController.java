package com.example.learnhub.Controller;


import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Image;
import com.example.learnhub.InterfaceOfControllers.InterfaceOfImageController;
import com.example.learnhub.Repository.ImageRepository;
import com.example.learnhub.ResponeObject.ResponeObject;
import com.example.learnhub.Service.ServiceOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ImageController implements InterfaceOfImageController {

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    Path staticPath = Paths.get("static");
    Path imagePath = Paths.get("images");

    ImageRepository imageRepository;
    ServiceOfFile serviceOfFile;


    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("text/xml")
                || contentType.equals("application/pdf")
                || contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    public String getImageName(MultipartFile image) throws IOException {
        String randomName = UUID.randomUUID().toString().substring(0, 5)+image.getOriginalFilename();
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(randomName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        return randomName;
    }

    public ResponseEntity<ResponeObject> saveCourseImages(@RequestParam("images") List<MultipartFile> images, @RequestParam int courseId) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            serviceOfFile.uploadFile(image);
            imageUrls.add(image.getOriginalFilename()); // Use the filename directly as the URL
        }

        if (imageRepository.existsByCourse_CourseId(courseId)) {
            for (String imageUrl : imageUrls) {
                imageRepository.updateImages(imageUrl, courseId);
            }
        } else {
            // Save new images if the course doesn't have images
            for (String imageUrl : imageUrls) {
                Image image = new Image();
                image.setImageUrl(imageUrl);

                Course course = new Course();
                course.setCourseId(courseId);
                imageRepository.save(image);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponeObject("ok", "Images updated successfully!!", imageUrls)
        );
    }


}