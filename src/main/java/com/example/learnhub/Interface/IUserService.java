package com.example.learnhub.Interface;

import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.DataNotFoundException;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String email, String password);
}
