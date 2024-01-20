package com.example.learnhub.Controller.user;


import com.example.learnhub.DTO.common.enums.ErrorMessage;
import com.example.learnhub.DTO.common.response.ApiResponse;
import com.example.learnhub.DTO.user.request.UserLoginRequest;
import com.example.learnhub.DTO.user.request.UserRegisterRequest;
import com.example.learnhub.DTO.user.request.UserVerifyOTPRequest;
import com.example.learnhub.DTO.user.response.UserLoginResponse;
import com.example.learnhub.DTO.user.response.UserResponse;
import com.example.learnhub.Entity.Role;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.BusinessException;
import com.example.learnhub.Exceptions.UnauthorizeException;
import com.example.learnhub.Repository.RoleRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.mailv2.model.Mail;
import com.example.learnhub.mailv2.service.EmailSenderService;
import com.example.learnhub.security.UserDetailsImpl;
import com.example.learnhub.security.jwt.JWTUtils;
import com.example.learnhub.security.utils.AESUtils;
import com.example.learnhub.security.utils.RandomStringGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserV1Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Value("${aes.key}")
    private String key;

    @Value("${spring.mail.username}")
    private String mailFrom;


    //TODO: Register
    @PostMapping("/v1/register")
    ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody UserRegisterRequest request){

        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if(Objects.nonNull(user) && user.getEnable().equals(Boolean.TRUE)){
                throw new BusinessException(ErrorMessage.USER_EMAIL_EXISTED);
            }

            List<Role> roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.STUDENT.name());
            String randomString = RandomStringGenerator.generateRandomString(6);
            user = new User()
                .setEmail(request.getEmail())
                .setFacebook(request.getFacebook())
                .setFullName(request.getFullname())
                .setUserPassword(AESUtils.encrypt(request.getPassword(),key))
                .setRoleId(roles.get(0).getRoleId())
                .setEnable(Boolean.FALSE)
                .setImage("url")
                .setToken("token")
                .setStringRandom(randomString);
            user = userRepository.save(user);

            // send email:
            log.info("START... Sending email");
            Mail mail = new Mail();
            mail.setFrom(mailFrom);//replace with your desired email
            mail.setTo(user.getEmail());//replace with your desired email
            mail.setSubject("Email verify Leanhub!");
            Map<String, Object> model = new HashMap<>();
            model.put("otp_value", randomString);
            mail.setPros(model);
            mail.setTemplate("index");
            emailSenderService.sendEmail(mail);
            log.info("END... Email sent success");
            ApiResponse<UserResponse> response = new ApiResponse<>().ok(new UserResponse(user,roles.get(0)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_CREATE_FAIL);
        }
    }
    //TODO: verify mail


    @PostMapping("/v1/verify")
    ResponseEntity<ApiResponse<UserResponse>> verify(@RequestBody UserVerifyOTPRequest request){
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);

            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }

            if(user.getEnable().equals(Boolean.TRUE)){
                throw new BusinessException(ErrorMessage.USER_ENABLED);
            }

            user.setEnable(Boolean.TRUE);
            user = userRepository.save(user);
            Role role = roleRepository.findById(user.getRoleId()).orElse(null);
            ApiResponse<UserResponse> response = new ApiResponse<>().ok(new UserResponse(user,role));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_VERIFY_CHALLENGE_FAILED);
        }
    }
    //TODO: Login

    @PostMapping("/v1/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request){
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (Objects.isNull(user)) {
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }
            if (!AESUtils.decrypt(user.getUserPassword(), key).equals(request.getPassword())) {
                throw new UnauthorizeException(ErrorMessage.USER_AUTHORIZATION_FAILED);
            }
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String accessToken = JWTUtils.generateAccessToken(userDetails, user);
            String refreshToken = JWTUtils.generateRefreshToken(userDetails);
            UserLoginResponse response = new UserLoginResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
            ApiResponse<UserLoginResponse> responseBody = new ApiResponse<>().ok(response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_LOGIN_FAIL);
        }
    }

    //TODO: UpdateUser
    //TODO: Forgot pass
    //TODO: Check forgot pass
}
