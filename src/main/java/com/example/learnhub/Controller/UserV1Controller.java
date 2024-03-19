package com.example.learnhub.Controller;


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
import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Service.ServiceOfImage;
import com.example.learnhub.mailv2.model.Mail;
import com.example.learnhub.mailv2.service.EmailSenderService;
import com.example.learnhub.security.UserDetailsImpl;
import com.example.learnhub.security.jwt.JWTUtils;
import com.example.learnhub.security.utils.AESUtils;
import com.example.learnhub.security.utils.RandomStringGenerator;
import com.example.learnhub.utils.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin("*")
public class UserV1Controller {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailSenderService emailSenderService;


    private final ServiceOfFile fileService;

    private final ServiceOfImage serviceOfImage;

    @Value("${aes.key}")
    private String key;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public UserV1Controller(UserRepository userRepository, RoleRepository roleRepository, EmailSenderService emailSenderService, ServiceOfFile fileService, ServiceOfImage serviceOfImage) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSenderService = emailSenderService;
        this.fileService = fileService;
        this.serviceOfImage = serviceOfImage;
    }

    record UserChange(String emailOrigin, String emailChange,String otp) {}
    private static final Map<String, UserChange> map = new HashMap<>();

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

            List<Role> roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.STUDENT.name());
            String randomString = RandomStringGenerator.generateRandomString(6);
            user = new User()
                .setEmail(request.getEmail())
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

            assert user != null;
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
            assert user != null;
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

            assert user != null;
            if(!request.getOtp().equals(user.getStringRandom())){
                throw new BusinessException(ErrorMessage.USER_OTP_NOT_MATCH);
            }

            user.setStringRandom("");
            user.setUserPassword(AESUtils.encrypt(request.getNewPassword(),key));
            userRepository.save(user);
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
            assert user != null;
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
            assert user != null;
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
        assert user != null;
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
// Todo: Soft Delete
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


            Role roleUserDisabled = roleRepository.findById(user.getRoleId()).orElse(null);

            if(Objects.nonNull(roleUserDisabled) && roleUserDisabled.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_CAN_NOT_DELETE_ADMIN);
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

// Todo: Restore user
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
    @PostMapping(value = "/v1/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<Object>> uploadAvatar(Principal principal, @RequestParam("file") MultipartFile file) {
        try {
            User user = getUserAvailable(principal.getName(), false);

            // Check if the user already has an image and delete it if it exists
            assert user != null;
            if (Objects.nonNull(user.getImage()) && !user.getImage().equals("url")) {
                try {
                    fileService.deleteFile(user.getImage());
                } catch (Exception e) {
                    log.error("Failed to delete file: {}", user.getImage());
                }
            }

            // Save the uploaded image and get the URL
            String url = serviceOfImage.saveImage(file);

            // Update the user's image with the URL
            user.setImage(url);
            userRepository.save(user);

            // Return the URL of the uploaded image in the response
            return new ResponseEntity<>(new ApiResponse<>().ok(FileUtils.getFileUrl(user.getImage())), HttpStatus.OK);
        } catch (BusinessException e) {
            // Rethrow BusinessException
            throw e;
        } catch (Exception e) {
            // Log and handle unexpected exceptions
            log.error("Upload avatar failed with error: {}", e.getMessage());
            throw new BusinessException(ErrorMessage.USER_UPLOAD_AVATAR_FAILED);
        }
    }


// Todo: Add courseManager
       @PostMapping("/v1/addCourseManage")
    ResponseEntity<ApiResponse<UserResponse>> addCourseManageUser(Principal principal, @RequestBody AdminAddCourseManagerRequest request) {
        try {
            User userRequest = getUserAvailable(principal.getName(), true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
            if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            User userCreated = getUserAvailable(request.getEmail(),true);
            if(Objects.nonNull(userCreated)){
                throw new BusinessException(ErrorMessage.USER_EMAIL_EXISTED);
            }

            List<Role> roles = roleRepository.findByRoleName(com.example.learnhub.security.Role.COURSEMANAGER.name());

            userCreated = new User()
                    .setEmail(request.getEmail())
                    .setFullName(request.getFullname())
                    .setUserPassword(AESUtils.encrypt(request.getPassword(),key))
                    .setRoleId(roles.get(0).getRoleId())
                    .setEnable(Boolean.TRUE)
                    .setImage("url")
                    .setToken("token")
                    .setDeleted(false)
                    .setStringRandom("");
            userCreated = userRepository.save(userCreated);
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(userCreated,roles.get(0)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_CREATE_FAIL);
        }
    }


// Todo: Change email ( only admin)
    @PostMapping("/v1/changeEmail")
    ResponseEntity<ApiResponse<CommonStatusResponse>> requestToChangeEmail(Principal principal, @Validated @RequestBody AdminRequestToChangeEmailRequest request) {
        try {
            User userRequest = getUserAvailable(principal.getName(), true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
            if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            User user = getUserAvailable(request.getEmailOrigin(),false);

            User userChanged = getUserAvailable(request.getEmailChange(),true);
            if(Objects.nonNull(userChanged)){
                throw new BusinessException(ErrorMessage.USER_EMAIL_EXISTED);
            }

            String randomString = RandomStringGenerator.generateRandomString(6);


            assert user != null;
            UserChange userChange = new UserChange(user.getEmail(), request.getEmailChange(),randomString);
            map.put(request.getEmailChange(), userChange);


            // send email:
            log.info("START... Sending email");
            Mail mail = new Mail();
            mail.setFrom(mailFrom);//replace with your desired email
            mail.setTo(request.getEmailChange());//replace with your desired email
            mail.setSubject("Email verify Leanhub to change email !");
            Map<String, Object> model = new HashMap<>();
            model.put("otp_value", randomString);
            mail.setPros(model);
            mail.setTemplate("index");
            emailSenderService.sendEmail(mail);
            log.info("END... Email sent success");

            ApiResponse<CommonStatusResponse> response = new ApiResponse<CommonStatusResponse>().ok(new CommonStatusResponse(true));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_REQUEST_CHANGE_EMAIL_FAIL);
        }
    }


    // Todo: Verify email change
    @PostMapping("/v1/verifyEmailChange")
    ResponseEntity<ApiResponse<UserResponse>> verifyEmailChange(Principal principal, @Validated @RequestBody AdminVerifyEmailChangeRequest request) {
        try {
            User userRequest = getUserAvailable(principal.getName(), true);
            if(Objects.isNull(userRequest)){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }

            Role role = roleRepository.findById(userRequest.getRoleId()).orElse(null);
            if(Objects.isNull(role) || !role.getRoleName().equals(com.example.learnhub.security.Role.ADMIN.name())){
                throw new BusinessException(ErrorMessage.USER_DO_NOT_PERMISSION);
            }


            UserChange userChangeInMap = map.get(request.getEmailChange());
            if(Objects.isNull(userChangeInMap)) {
                throw new BusinessException(ErrorMessage.USER_REQUEST_INVALID);
            }

            User user = getUserAvailable(userChangeInMap.emailOrigin(),false);

            User userChanged = getUserAvailable(userChangeInMap.emailChange(),true);

            if(Objects.nonNull(userChanged)){
                throw new BusinessException(ErrorMessage.USER_EMAIL_EXISTED);
            }

            if(!Objects.equals(userChangeInMap.otp(), request.getOtp())) {
                throw new BusinessException(ErrorMessage.USER_OTP_NOT_MATCH);
            }

            assert user != null;
            user.setEmail(request.getEmailChange());
            user = userRepository.save(user);
            Role roleUser = roleRepository.findById(user.getRoleId()).orElse(null);

            map.remove(request.getEmailChange());
            ApiResponse<UserResponse> response = new ApiResponse<UserResponse>().ok(new UserResponse(user,roleUser));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_REQUEST_CHANGE_EMAIL_FAIL);
        }
    }

    //TODO: GET List deleted user
    @GetMapping("/v1/listDeleted")
    ResponseEntity<ApiResponse<List<UserInfoResponse>>> listDeleteUser(){
        try {
            List<User> userList = userRepository.findAllByDeleted(true);
            ApiResponse<List<UserInfoResponse>> response = new ApiResponse<List<UserInfoResponse>>().ok(userList.stream().map(UserInfoResponse::new).collect(Collectors.toList()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error: {}", e.getLocalizedMessage());
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }

    }



}
