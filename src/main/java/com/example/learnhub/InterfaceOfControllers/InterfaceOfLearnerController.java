package com.example.learnhub.InterfaceOfControllers;
import com.example.learnhub.DTO.UserDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RequestMapping("/learner")
public interface InterfaceOfLearnerController {

    @PostMapping("/showStudentPurchase")
    ResponseEntity<List<User>> showStudentPurchase(@RequestParam Integer accountId);

    @PostMapping("/addLearner")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO createLearner(@RequestBody @Valid UserDTO dto) throws AppServiceExeption, IOException;

    @GetMapping("/getLearners")
    List<UserDTO> getLearnerList();

    @PostMapping("/countLearnerByOwner")
    @ResponseStatus(HttpStatus.CREATED)
    int countLearner(int owner) throws AppServiceExeption, IOException;


    @PostMapping("/GetProfitByOwner")
    @ResponseStatus(HttpStatus.CREATED)
    double getProfitByOwner(int owner) throws AppServiceExeption, IOException;


}