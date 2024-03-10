package com.example.learnhub.Exceptions;

public class QuestionNotFoundException extends  RuntimeException {
    public QuestionNotFoundException(int id){
        super("Question with ID:"+id+"do not have any Questions!!");
    }

}
