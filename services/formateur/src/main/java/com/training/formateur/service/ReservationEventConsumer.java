package com.training.formateur.service;


import com.etraining.events.ReservationCancelledEvent;
import com.etraining.events.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventConsumer {

    @KafkaListener(topics = "reservation-created", groupId = "formateur-service")
    public void handleReservationCreated(ReservationCreatedEvent event) {
        log.info("Received ReservationCreatedEvent for formateur: {}",
                event.getFormateurKeycloakId());
        // Mettre à jour la disponibilité du formateur
    }

    @KafkaListener(topics = "reservation-cancelled", groupId = "formateur-service")
    public void handleReservationCancelled(ReservationCancelledEvent event) {
        log.info("Received ReservationCancelledEvent: {}", event.getReservationId());
        // Libérer la disponibilité du formateur
    }
}
