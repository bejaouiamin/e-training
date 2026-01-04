package com.etraining.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreatedEvent {
    private Long reservationId;
    private Long salleId;
    private String salleName;
    private String formateurKeycloakId;
    private Long sessionFormationId;
    private Instant startDateTime;
    private Instant endDateTime;
    private Instant createdAt;
}
