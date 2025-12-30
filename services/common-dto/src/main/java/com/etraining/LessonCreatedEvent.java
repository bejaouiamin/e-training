package com.etraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreatedEvent {
    private String eventId;
    private String authorKeycloakId;
    private Long themeId;             // ID du theme côté cours-service
    private String title;
    private String description;
    private Integer sequenceOrder;
    private List<ResourceDto> resources; // ✅ Utiliser ResourceDto du package com.etraining

}

