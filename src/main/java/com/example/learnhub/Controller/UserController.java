package com.example.learnhub.Controller;


 import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.MailConfig.MailDetail;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.MailConfig.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("/users")
    public ResponseEntity<Object> createAccount(@RequestBody User user) {
        try {
            Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
            Optional<User> userByUserName = userRepository.findByUserName(user.getUserName());

            if (userByEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Email already exists");
            } else if (userByUserName.isPresent()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Username already exists");
            } else {
                if (user.getToken() != null) {
                    boolean sendToken = sendMailToReceiver(user.getEmail(), user.getToken());
                }

                User _user = userRepository.save(new User(
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserPassword(),
                        user.getImage(),
                        user.getFacebook(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getToken(),
                        user.getRoleId()
                ));

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(_user);
            }
        } catch (Exception | AppServiceExeption e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
        }
    }


    private boolean sendMailToReceiver(String email, String token) throws AppServiceExeption, IOException {
        String messageBody = "Hello,\n" +
                "Please enter the verification code below on the LearnHub website\n\n" +
                token + "\n\n" +
                "If you didn't request this, you can ignore this email or let us know.\n" +
                "Thanks! LearnHub team";

        String subject = "LearnHub OTP verification";

        MailDetail mailDetail = new MailDetail();
        mailDetail.setMsgBody(messageBody);
        mailDetail.setRecipient(email);
        mailDetail.setSubject(subject);
        mailService.sendMail(mailDetail);

        return true;
    }
    @PostMapping("/users/checkToken")
    public ResponseEntity<User> checkToken(@RequestParam int id, @RequestParam String token) {
        Optional<User> userAccount = userRepository.findById(id);
        if (userAccount.isPresent()) {
            if (userAccount.get().getToken().equals(token)) {
                int updateToken = userRepository.updateToken(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //LOGIN
    @PostMapping("/users/login")
    public ResponseEntity<UserDTO> loginAccount(@RequestParam String email, @RequestParam String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getUserPassword().equals(password)) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserID(user.getUserId());
                userDTO.setFullName(user.getFullName());
                userDTO.setImage(user.getImage());
                userDTO.setEmail(user.getEmail());
                userDTO.setRoleId(user.getRoleId());
                userDTO.setFacebook(user.getFacebook());

                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            } else {
                // Incorrect password
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            // User not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //UPDATE
    @PutMapping("/users")
    public ResponseEntity<User> updateAccount(@RequestBody User user) {
        Optional<User> userData = userRepository.findById(user.getUserId());

        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setFullName(user.getFullName());
            if (user.getUserPassword() == null) {
                _user.setUserPassword(_user.getUserPassword());
            } else {
                _user.setUserPassword(user.getUserPassword());
            }

            _user.setImage(_user.getImage());
            _user.setEmail(user.getEmail());
            _user.setRoleId(user.getRoleId());
            _user.setFacebook(user.getFacebook());

            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") int id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/users/role")
    public ResponseEntity<List<User>> findByRole() {
        try {
            List<User> users = userRepository.findByRoleId(1);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

