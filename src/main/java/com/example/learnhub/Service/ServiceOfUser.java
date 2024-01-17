package com.example.learnhub.Service;

import com.example.learnhub.Exceptions.AccountNotFoundException;
import com.example.learnhub.DTO.CreateUserDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Interface.InterfaceOfUser;
import com.example.learnhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfUser implements InterfaceOfUser {
    @Override
    public List<UserDTO> getAccounts() {
        return null;
    }

    @Autowired
    UserRepository userRepository;

    @Override
    public User createAccount(CreateUserDTO dto) throws AppServiceExeption {
        if(userRepository.existsByEmail(dto.getEmail()))
        {
            throw new AppServiceExeption("User already existed!");
        }
        User user = new User();
        user.setImage(dto.getImage());
        user.setUserPassword(dto.getPassword());
        user.setFacebook(dto.getFacebook());
        return  userRepository.save(user);
    }

    @Override
    public UserDTO createAccount(String email, String fullName, MultipartFile image, String pass) throws AppServiceExeption, IOException {
        return null;
    }




    @Override
    public Optional<User> getAccountByCourseID(int id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public User getAccountByID(int id) {
        return userRepository.findByUserId(id).orElseThrow(() -> new AccountNotFoundException(id));
    }


}