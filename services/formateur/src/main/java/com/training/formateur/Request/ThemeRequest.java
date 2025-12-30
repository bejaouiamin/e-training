package com.training.formateur.Request;

import lombok.Data;

@Data
public class ThemeRequest {
    private String keycloakId;
    private String title;
    private String description;
    private Long categoryId;
    private Integer dureeHeures;
}