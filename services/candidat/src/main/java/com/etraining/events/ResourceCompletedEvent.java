package com.etraining.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCompletedEvent {
    private String keycloakId;
    private Long lessonId;
    private Long resourceId;
    private Integer totalResourcesInLesson;
    private String source = "cours-service";
}
