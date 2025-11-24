package com.training.formateur.entites;

import lombok.*;

import java.time.LocalTime;

@Data
public class DisponibiliteJour {
    private boolean disponible;
    private LocalTime heureDebut;
    private LocalTime heureFin;

}
