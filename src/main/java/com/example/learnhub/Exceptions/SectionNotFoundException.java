package com.example.learnhub.Exceptions;

public class SectionNotFoundException extends  RuntimeException{
    public SectionNotFoundException(int id){
        super("Section with ID:"+id+"do not have any Section!!");
    }

}
