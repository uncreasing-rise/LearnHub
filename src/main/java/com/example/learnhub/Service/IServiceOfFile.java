package com.example.learnhub.Service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IServiceOfFile {

    List<String> listOfFiles();

    ByteArrayResource downloadFile(String fileName);

    boolean deleteFile(String fileName);


    String uploadFile(MultipartFile file) throws IOException;

    String uploadVideo(MultipartFile videoFile) throws IOException;

    String uploadImage(MultipartFile imageFile) throws IOException;

    String constructFileUrl(String originalFilename);
}
