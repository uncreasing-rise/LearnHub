package com.example.learnhub.InterfaceOfControllers;
import com.example.learnhub.ResponeObject.ResponeObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/image")
public interface InterfaceOfImageController {

    @PostMapping("/saveCourseImages")
    ResponseEntity<ResponeObject> saveCourseImages(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam int courseId) throws IOException;

}
