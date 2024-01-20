package com.example.learnhub.mailv2.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class Mail {
    private String from;
    private String to;
    private String subject;
    private Map<String, Object> pros;
    private String template;
}
