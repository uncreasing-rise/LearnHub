package com.example.learnhub.Controller;


import com.example.learnhub.Entity.User;
import com.example.learnhub.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learnhub.DTO.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid@RequestBody UserDTO userDTO,
            BindingResult result
            ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("Register Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid@RequestBody User userLoginDTO){
        return ResponseEntity.ok("some token");
    }
}
