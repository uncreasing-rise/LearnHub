package com.example.learnhub.Controller;


 import com.example.learnhub.DTO.UserDTO;
 import com.example.learnhub.DTO.user.request.UserLoginRequest;
 import com.example.learnhub.DTO.user.response.UserLoginResponse;
 import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.MailConfig.MailDetail;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.MailConfig.MailService;
 import com.example.learnhub.security.UserDetailsImpl;
 import com.example.learnhub.security.jwt.JWTUtils;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
 import java.util.Objects;
 import java.util.Optional;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody User user) {
        try {
            Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

            if (userByEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Email already exists");
            }  else {
                if (user.getToken() != null) {
                    boolean sendToken = sendMailToReceiver(user.getEmail(), user.getToken());
                }

                User _user = userRepository.save(new User(
                        user.getUserId(),
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

    //UPDATE
    @PutMapping("/updates")
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


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if(Objects.isNull(user)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!user.getUserPassword().equals(request.getPassword())){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String accessToken = JWTUtils.generateAccessToken(userDetails, user);
            String refreshToken = JWTUtils.generateRefreshToken(userDetails);
            UserLoginResponse response = new UserLoginResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

