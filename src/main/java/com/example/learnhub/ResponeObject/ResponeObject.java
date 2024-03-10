package com.example.learnhub.ResponeObject;

import java.util.List;

public class ResponeObject {
     private String status;

     private String message;

     private Object data;

     public ResponeObject(){}

    public ResponeObject(String status, String message, Object data)
     {
         this.status = status;
         this.message = message;
         this.data = data;
     }

    public ResponeObject(String s) {
    }

    public ResponeObject(String imagesUploadedSuccessfully, List<String> imageUrls) {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
