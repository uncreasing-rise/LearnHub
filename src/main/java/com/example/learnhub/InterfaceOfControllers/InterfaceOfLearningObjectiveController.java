package com.example.learnhub.InterfaceOfControllers;


import com.example.learnhub.DTO.LearningDetailDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/learningObjective")
public interface InterfaceOfLearningObjectiveController {

    @PostMapping("/addLearningObjective")
    @ResponseStatus(HttpStatus.CREATED)
    LearningDetailDTO createLearningObjective(@RequestBody @Valid LearningDetailDTO dto) throws AppServiceExeption, IOException;


    @GetMapping("/getLearningObjective")
    List<LearningDetailDTO> getLearningObjectives();
}
