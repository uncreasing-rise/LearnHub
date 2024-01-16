package com.example.learnhub.Controller;

import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.MailConfig.MailDetail;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.MailConfig.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
}
