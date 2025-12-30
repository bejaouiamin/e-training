package com.etraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {
    private String title;
    private String url;
    private ResourceType type; // VIDEO, PDF, QUIZ
    private Integer passingScore;
}
