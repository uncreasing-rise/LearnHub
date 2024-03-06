package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfSection {

    private final SectionRepository sectionRepository;
    private final ServiceOfVideo serviceOfVideo;
    private final ServiceOfArticle serviceOfArticle;

    @Autowired
    public ServiceOfSection(SectionRepository sectionRepository, ServiceOfVideo serviceOfVideo, ServiceOfArticle serviceOfArticle) {
        this.sectionRepository = sectionRepository;
        this.serviceOfVideo = serviceOfVideo;
        this.serviceOfArticle = serviceOfArticle;
    }

    public Section createSection(SectionDTO sectionDTO, List<MultipartFile> videoFiles, List<ArticleDTO> articleDTOs) {
        Section section = new Section();
        section.setSectionName(sectionDTO.getSectionName());
        // Set other properties as needed

        // Check if there are videos or articles to associate with the section
        if (videoFiles != null && !videoFiles.isEmpty()) {
            serviceOfVideo.createVideos(section, videoFiles);
        }

        if (articleDTOs != null && !articleDTOs.isEmpty()) {
            serviceOfArticle.createArticles(section, articleDTOs);
        }

        // Save the section
        return sectionRepository.save(section);
    }



    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public void createVideos(Section section, List<MultipartFile> videoFiles) {
        Section savedSection = sectionRepository.save(section);

        if (videoFiles != null) {
            serviceOfVideo.createVideos(savedSection, videoFiles);
        }
    }

    public void createArticles(Section section, List<ArticleDTO> articleDTOs) {
        Section savedSection = sectionRepository.save(section);

        if (articleDTOs != null) {
            serviceOfArticle.createArticles(savedSection, articleDTOs);
        }
    }

    public Optional<Section> getSectionById(Integer id) {
        return sectionRepository.findById(id);
    }

    @Transactional
    public Section updateSection(Integer id, SectionDTO updatedSection) {
        Optional<Section> optionalExistingSection = sectionRepository.findById(id);
        if (optionalExistingSection.isPresent()) {
            Section existingSection = optionalExistingSection.get();
            existingSection.setSectionName(updatedSection.getSectionName());
            // Update other properties as needed...
            return sectionRepository.save(existingSection);
        } else {
            throw new EntityNotFoundException("Section not found with id: " + id);
        }
    }

    @Transactional
    public boolean deleteSection(Integer id) {
        sectionRepository.deleteById(id);
        return false;
    }
}
