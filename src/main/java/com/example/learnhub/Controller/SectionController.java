package com.example.learnhub.Controller;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Service.ServiceOfSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final ServiceOfSection sectionService;

    @Autowired
    public SectionController(ServiceOfSection sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Section> createSection(@RequestBody SectionDTO sectionDTO,
                                                 @RequestParam(required = false) List<MultipartFile> videoFiles,
                                                 @RequestParam(required = false) List<ArticleDTO> articleDTOs) {
        Section createdSection = sectionService.createSection(sectionDTO, videoFiles, articleDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Section>> getSectionById(@PathVariable("id") Integer id) {
        Optional<Section> sectionDTO = sectionService.getSectionById(id);
        if (sectionDTO.isPresent()) {
            return ResponseEntity.ok(sectionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable("id") Integer id, @RequestBody SectionDTO sectionDTO) {
        Section updatedSection = sectionService.updateSection(id, sectionDTO);
        if (updatedSection != null) {
            return ResponseEntity.ok(updatedSection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable("id") Integer id) {
        boolean deleted = sectionService.deleteSection(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Add more endpoints as needed
}
