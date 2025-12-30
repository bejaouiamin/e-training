package com.etraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeCreatedEvent {
    private String eventId;
    private String authorKeycloakId;
    private String title;
    private String description;
    private Long categoryId;
    private Integer dureeHeures;
}
