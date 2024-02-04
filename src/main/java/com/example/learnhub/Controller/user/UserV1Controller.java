package com.example.learnhub.Controller.user;


import com.example.learnhub.DTO.common.enums.ErrorMessage;
import com.example.learnhub.DTO.common.response.ApiResponse;
import com.example.learnhub.DTO.user.request.*;
import com.example.learnhub.DTO.user.response.CommonStatusResponse;
import com.example.learnhub.DTO.user.response.UserInfoResponse;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

            if(!userRepository.findByEmailAndDeleted(request.getEmail(), true).isEmpty()){
                throw new BusinessException(ErrorMessage.USER_EMAIL_INVALID);
            }

            User user = getUserAvailable(request.getEmail(),  true);

            if(Objects.nonNull(user)){
                throw new BusinessException(ErrorMessage.USER_EMAIL_EXISTED);
            }


            if(Boolean.TRUE.equals(user.getEnable())) {
                throw new BusinessException(ErrorMessage.USER_ENABLED);
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
                .setDeleted(false)
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
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,roles.get(0)));
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
            User user = getUserAvailable(request.getEmail(), false);

            if(!request.getOtp().equals(user.getStringRandom())){
                throw new BusinessException(ErrorMessage.USER_OTP_NOT_MATCH);
            }

            if(user.getEnable().equals(Boolean.TRUE)){
                throw new BusinessException(ErrorMessage.USER_ENABLED);
            }

            user.setEnable(Boolean.TRUE);
            user.setStringRandom("");
            user = userRepository.save(user);
            Role role = roleRepository.findById(user.getRoleId()).orElse(null);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,role));
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
            ApiResponse<UserLoginResponse> responseBody = loginUserRole(request.getEmail(), request.getPassword(),null);
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
    @PutMapping("/v1/{id}")
    ResponseEntity<ApiResponse<UserResponse>> updateUser(Principal principal,  @PathVariable(name = "id") String id, @RequestBody UserUpdateRequest request){
        try {

            User userRequest = getUserAvailable(principal.getName(),true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            User userUpdate = userRepository.findByUserId(Integer.valueOf(id)).orElse(null);
            if(Objects.isNull(userUpdate)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }

            if(!Objects.equals(userRequest.getUserId(), userUpdate.getUserId())) {
                Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
                if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                    throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
                }

                if(Objects.nonNull(request.getRoleId())){
                    Role roleNew = roleRepository.findById(request.getRoleId()).orElse(null);
                    if(Objects.isNull(roleNew)){
                        throw new BusinessException(ErrorMessage.USER_ROLE_NOT_FOUND);
                    }
                    userUpdate.setRoleId(roleNew.getRoleId());
                }

                if(Objects.nonNull(request.getEnable())){
                    userUpdate.setEnable(!userUpdate.getEnable());
                }
            }


            if(StringUtils.isNotBlank(request.getFullname())){
                userUpdate.setFullName(request.getFullname());
            }

            if(StringUtils.isNotBlank(request.getFacebook())){
                userUpdate.setFacebook(request.getFacebook());
            }

            if(StringUtils.isNotBlank(request.getImage())){
                userUpdate.setImage(request.getImage());
            }
            Role role = roleRepository.findById(userUpdate.getRoleId()).orElse(null);
            userUpdate = userRepository.save(userUpdate);
            ApiResponse<UserResponse> responseBody = new ApiResponse<UserResponse>().ok(new UserResponse(userUpdate,role));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_UPDATE_FAIL);
        }
    }


    //TODO: Forgot pass

    @PostMapping("/v1/forgetPassword")
    ResponseEntity<ApiResponse<CommonStatusResponse>> forgetPasswordRequest(@Validated @RequestBody ForgetPassRequest request){
        try {
            User user = getUserAvailable(request.getEmail(),false);
            String randomString = RandomStringGenerator.generateRandomString(6);
            user.setStringRandom(randomString);
            user = userRepository.save(user);
            // send email:
            log.info("START... Sending email to {}", user.getEmail());
            Mail mail = new Mail();
            mail.setFrom(mailFrom);//replace with your desired email
            mail.setTo(user.getEmail());//replace with your desired email
            mail.setSubject("Email Forget Password!");
            Map<String, Object> model = new HashMap<>();
            model.put("otp_value", randomString);
            mail.setPros(model);
            mail.setTemplate("index");
            emailSenderService.sendEmail(mail);
            log.info("END... Email sent success");
            ApiResponse<CommonStatusResponse> responseBody = new ApiResponse<CommonStatusResponse>().ok(new CommonStatusResponse(true));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_RESET_PASSWORD_FAIL);
        }
    }


    //TODO: Check forgot pass
    @PostMapping("/v1/verifyForgetPassword")
    ResponseEntity<ApiResponse<CommonStatusResponse>> verifyForgetPassword(@Validated @RequestBody VerifyForgetPasswordRequest  request){
        try {
            User user = getUserAvailable(request.getEmail(), false);

            if(!request.getOtp().equals(user.getStringRandom())){
                throw new BusinessException(ErrorMessage.USER_OTP_NOT_MATCH);
            }

            user.setStringRandom("");
            user.setUserPassword(AESUtils.encrypt(request.getNewPassword(),key));
            user = userRepository.save(user);
            ApiResponse<CommonStatusResponse> response = new ApiResponse<CommonStatusResponse>().ok(new CommonStatusResponse(true));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_VERIFY_CHALLENGE_FAILED);
        }
    }


    //TODO: Reset password
    @PostMapping("/v1/resetPassword")
    ResponseEntity<ApiResponse<CommonStatusResponse>> resetPassword(@RequestBody UserResetPasswordRequest request) {
        try {
            User user = getUserAvailable(request.getEmail(), false);
            String newPassword = user.getEmail().split("@")[0] + "123#";
            user.setUserPassword(AESUtils.encrypt(newPassword,key));
            user = userRepository.save(user);
            log.info("Reset password success");
            // send email:
            log.info("START... Sending email to {}", user.getEmail());
            Mail mail = new Mail();
            mail.setFrom(mailFrom);//replace with your desired email
            mail.setTo(user.getEmail());//replace with your desired email
            mail.setSubject("Email Reset Password!");
            Map<String, Object> model = new HashMap<>();
            model.put("new_password", newPassword);
            mail.setPros(model);
            mail.setTemplate("resetPassword");
            emailSenderService.sendEmail(mail);
            log.info("END... Email sent success");
            ApiResponse<CommonStatusResponse> responseBody = new ApiResponse<CommonStatusResponse>().ok(new CommonStatusResponse(true));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_RESET_PASSWORD_FAIL);
        }
    }


    //TODO: Get Info user
    @GetMapping("/v1/info")
    ResponseEntity<ApiResponse<UserResponse>> getInfoUser(Principal principal){
        try {
            User user = getUserAvailable(principal.getName(), false);
            Role role = roleRepository.findById(user.getRoleId()).orElse(null);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,role));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }


    //TODO: GET List user
    @GetMapping("/v1/list")
    ResponseEntity<ApiResponse<List<UserInfoResponse>>> listUser(){
        try {
            List<User> userList = userRepository.findAllByDeleted(false);
            ApiResponse<List<UserInfoResponse>> response = new ApiResponse<List<UserInfoResponse>>().ok(userList.stream().map(UserInfoResponse::new).collect(Collectors.toList()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }

    }

    // TODO: GET User Detail
    @GetMapping("/v1/info/{id}")
    ResponseEntity<ApiResponse<UserResponse>> getInfoUser(@PathVariable(name = "id")String id){
        try {
            User user = userRepository.findByUserId(Integer.valueOf(id)).orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }
            Role role = roleRepository.findById(user.getRoleId()).orElse(null);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,role));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }


    @SneakyThrows
    private ApiResponse<UserLoginResponse> loginUserRole(String email, String password, Role roleExpect) {
        User user = getUserAvailable(email,false);
        if(!user.getEnable()){
            throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
        }
        if (!AESUtils.decrypt(user.getUserPassword(), key).equals(password)) {
            throw new UnauthorizeException(ErrorMessage.USER_AUTHORIZATION_FAILED);
        }
        Role role = roleRepository.findById(user.getRoleId()).orElse(null);
        if(Objects.nonNull(roleExpect)) {
            if(Objects.isNull(role) || !roleExpect.getRoleName().equals(role.getRoleName())) {
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }
        }
        UserDetailsImpl userDetails = UserDetailsImpl.build(user, role.getRoleName());
        String accessToken = JWTUtils.generateAccessToken(userDetails, user);
        String refreshToken = JWTUtils.generateRefreshToken(userDetails);
        UserLoginResponse response = new UserLoginResponse()
            .setAccessToken(accessToken)
            .setRefreshToken(refreshToken);
        return new ApiResponse<UserLoginResponse>().ok(response);
    }

    @PostMapping("/v1/admin/login")
    ResponseEntity<ApiResponse<UserLoginResponse>> adminLogin(@Validated @RequestBody UserAdminLoginRequest request) {
        try {
            List<Role> role = roleRepository.findByRoleName(com.example.learnhub.security.Role.ADMIN.name());
            if(role.isEmpty()){
                throw new BusinessException(ErrorMessage.USER_ROLE_NOT_FOUND);
            }
            ApiResponse<UserLoginResponse> responseBody = loginUserRole(request.getEmail(),request.getPassword(),role.get(0));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_LOGIN_FAIL);
        }
    }


    @PostMapping("/v1/coursemanager/login")
    ResponseEntity<ApiResponse<UserLoginResponse>> courseManagerLogin(@Validated @RequestBody UserCourseManagerLoginRequest request) {
        try {
            List<Role> role = roleRepository.findByRoleName(com.example.learnhub.security.Role.COURSEMANAGER.name());
            if(role.isEmpty()){
                throw new BusinessException(ErrorMessage.USER_ROLE_NOT_FOUND);
            }
            ApiResponse<UserLoginResponse> responseBody = loginUserRole(request.getEmail(),request.getPassword(),role.get(0));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_LOGIN_FAIL);
        }
    }


    @PostMapping("/v1/student/login")
    ResponseEntity<ApiResponse<UserLoginResponse>> studentLogin(@Validated @RequestBody UserStudentLoginRequest request) {
        try {
            List<Role> role = roleRepository.findByRoleName(com.example.learnhub.security.Role.STUDENT.name());
            if(role.isEmpty()){
                throw new BusinessException(ErrorMessage.USER_ROLE_NOT_FOUND);
            }
            ApiResponse<UserLoginResponse> responseBody = loginUserRole(request.getEmail(),request.getPassword(),role.get(0));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_LOGIN_FAIL);
        }
    }

    @DeleteMapping("/v1/{id}")
    ResponseEntity<ApiResponse<UserResponse>> deleteUser(Principal principal, @PathVariable(name = "id")String id){
        try {

            User userRequest = getUserAvailable(principal.getName(), true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
            if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }


            User user = userRepository.findByUserId(Integer.valueOf(id)).orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }

            user.setDeleted(true);
            user = userRepository.save(user);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,roleRepository.findById(user.getRoleId()).orElse(null)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_DISABLED_FAILED);
        }
    }


    @PostMapping("/v1/restore/{id}")
    ResponseEntity<ApiResponse<UserResponse>> restoreUser(Principal principal, @PathVariable(name = "id")String id){
        try {

            User userRequest = getUserAvailable(principal.getName(), true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
            if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }


            User user = userRepository.findByUserId(Integer.valueOf(id)).orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }

            user.setDeleted(false);
            user = userRepository.save(user);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,roleRepository.findById(user.getRoleId()).orElse(null)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_RESTORE_FAILED);
        }
    }


    private User getUserAvailable(String email, boolean nullable) {
        List<User> userList = userRepository.findByEmailAndDeleted(email, false);
        if(userList.isEmpty()){
            userList = userRepository.findByEmailAndDeleted(email,null);
            for (User user : userList){
                user.setDeleted(false);
            }
            userRepository.saveAll(userList);
        }
        if (userList.isEmpty()) {
            if(nullable){
                return  null;
            }
            throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
        }
        return userList.get(0);
    }

}
