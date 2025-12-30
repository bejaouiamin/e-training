package com.training.formateur.Request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResourceRequest {

    private String title;

    private MultipartFile file; // ✅ Fichier uploadé
    private String url;

    private String type; // VIDEO, PDF, QUIZ

    private Integer passingScore; // Optionnel, requis seulement pour QUIZ
}
