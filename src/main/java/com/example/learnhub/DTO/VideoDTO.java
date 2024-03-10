package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoDTO {
    private Integer videoId;
    private String videoUrl;
    private String Description;
    private Boolean isTrial;
    private Section section;
    private String Title;

}
