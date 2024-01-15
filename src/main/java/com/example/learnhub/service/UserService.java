package com.example.learnhub.service;

import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Entity.Role;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.DataNotFoundException;
import com.example.learnhub.Repository.RoleRepository;
import com.example.learnhub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email đã tồn tại");
        }
        // Convert from userDTO => user
        User newUser = User.builder()
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .userName(userDTO.getUserName())
                .facebook(userDTO.getFacebook())
                .userPassword(userDTO.getUserPassword())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        if ("0".equals(userDTO.getFacebook()) && "0".equals(userDTO.getEmail())) {
            String password = userDTO.getUserPassword();
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) {
        // Implement your login logic here
        return null;
    }
}
