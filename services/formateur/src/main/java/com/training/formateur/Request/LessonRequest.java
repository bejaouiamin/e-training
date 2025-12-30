package com.training.formateur.Request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class LessonRequest {
    private String keycloakId;
    private Long themeId;
    private String title;
    private String description;
    private Integer sequenceOrder;
    private List<String> resourceTitles;
    private List<String> resourceTypes;
    private List<MultipartFile> files;
}

