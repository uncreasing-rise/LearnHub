package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Section")
public class Section {
    @Id
    @Column(name = "SectionID")
    private Integer sectionId;

    @Column(name = "SectionName")
    private String sectionName;

    @Column(name = "CourseID")
    private Integer courseId;

}
