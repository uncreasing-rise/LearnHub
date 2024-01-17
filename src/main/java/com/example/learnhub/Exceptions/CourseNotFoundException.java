package com.example.learnhub.Exceptions;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(int id){
        super("Instructor with ID:"+id+"do not have any courses!!");
    }
}
