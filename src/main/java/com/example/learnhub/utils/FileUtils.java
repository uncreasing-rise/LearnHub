package com.example.learnhub.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {


    @Value("${gcp.bucket.name}")
    private String name;

    private static String NAME_BUCKET;

    @Value("${gcp.bucket.name}")
    public void setNameBucket(String name){
        FileUtils.NAME_BUCKET = name;
    }


    public static String getFileUrl(String fileName) {
        return "https://storage.googleapis.com/"+NAME_BUCKET+"/" + fileName;
    }
}
