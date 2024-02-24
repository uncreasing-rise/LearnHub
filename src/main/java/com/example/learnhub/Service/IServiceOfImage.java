package com.example.learnhub.Service;

import com.example.learnhub.Entity.Image;

public interface IServiceOfImage {

    Image getImageByCourseID(int id);

    int deleteImageByCourseID(int id);
}
