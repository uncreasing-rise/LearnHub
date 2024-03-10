package com.example.learnhub.Exceptions;

public class QuizNotFoundException extends  RuntimeException {
    public QuizNotFoundException(int id){
        super("Quiz with ID:"+id+"do not have any Quizzes!!");
    }

}
