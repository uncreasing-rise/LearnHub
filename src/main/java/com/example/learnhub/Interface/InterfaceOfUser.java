package com.example.learnhub.Interface;
import com.example.learnhub.DTO.CreateUserDTO;
import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.AppServiceExeption;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/accounts")
public interface InterfaceOfUser {
    User createAccount(CreateUserDTO dto) throws com.example.learnhub.Exceptions.AppServiceExeption;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO createAccount(String username, String firstname, String lastname, MultipartFile image, String pass) throws AppServiceExeption, IOException, com.example.learnhub.Exceptions.AppServiceExeption;



    @GetMapping("/")
    List<UserDTO> getAccounts();

    Optional<User> getAccountByCourseID(int id);

    User getAccountByID(int id);
}
