package com.etraining;

import lombok.Data;

import java.time.LocalTime;

@Data
public class DisponibiliteJour {
    private boolean disponible;
    private LocalTime heureDebut;
    private LocalTime heureFin;

}
