package com.example.learnhub.Exceptions;

public class ArticleNotFoundException extends  RuntimeException {
    public ArticleNotFoundException(int id){
        super("Article with ID:"+id+"do not have any Articles!!");
    }

}
