package com.example.learnhub.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:1433/")
@RestController
@RequestMapping("/api")
public class AccountController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

}
