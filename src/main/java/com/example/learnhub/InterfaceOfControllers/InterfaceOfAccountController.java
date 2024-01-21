package com.example.learnhub.InterfaceOfControllers;

import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/accounts")
public interface InterfaceOfAccountController {
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO createAccount(String username, String firstname, String lastname, MultipartFile image, String pass) throws AppServiceExeption, IOException;


    @GetMapping("/")
    List<UserDTO> getAccounts();
}
