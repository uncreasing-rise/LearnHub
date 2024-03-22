package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
@Entity
@Table(name = "Blog")
public class Blog {
    @GeneratedValue
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Category category;


    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    private String ImageUrl;

}
