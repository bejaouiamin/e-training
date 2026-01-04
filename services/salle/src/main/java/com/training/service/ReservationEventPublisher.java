package com.training.service;


import com.etraining.events.ReservationCancelledEvent;
import com.etraining.events.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationEventPublisher {

    private static final String TOPIC_RESERVATION_CREATED = "reservation-created";
    private static final String TOPIC_RESERVATION_CANCELLED = "reservation-cancelled";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReservationCreated(ReservationCreatedEvent event) {
        log.info("Publishing ReservationCreatedEvent: {}", event.getReservationId());
        kafkaTemplate.send(TOPIC_RESERVATION_CREATED,
                event.getReservationId().toString(), event);
    }

    public void publishReservationCancelled(ReservationCancelledEvent event) {
        log.info("Publishing ReservationCancelledEvent: {}", event.getReservationId());
        kafkaTemplate.send(TOPIC_RESERVATION_CANCELLED,
                event.getReservationId().toString(), event);
    }
}
