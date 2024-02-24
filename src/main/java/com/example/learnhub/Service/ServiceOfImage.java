package com.example.learnhub.Service;

import com.example.learnhub.Entity.Image;
import com.example.learnhub.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfImage implements IServiceOfImage { // Assuming there is an interface
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image getImageByCourseID(int courseId) {
        return imageRepository.findByCourse_CourseId(courseId);
    }
    @Override
    public int deleteImageByCourseID(int courseId) {
        return imageRepository.deleteImageByCourseId(courseId);
    }
}
