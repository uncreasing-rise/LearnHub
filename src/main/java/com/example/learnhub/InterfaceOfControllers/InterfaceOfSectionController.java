package com.example.learnhub.InterfaceOfControllers;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.ResponeObject.ResponeObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/section")
public interface InterfaceOfSectionController {

    @PostMapping("/addSection")
    @ResponseStatus(HttpStatus.CREATED)
    SectionDTO createSection(@RequestBody @Valid SectionDTO dto, int courseID , int accountID) throws AppServiceExeption, IOException;


    @GetMapping("/getSections")
    List<SectionDTO> getSections();

    @GetMapping("/getSection/{id}")
    ResponseEntity<ResponeObject> getSectionByCourseID(@PathVariable int id);


}
