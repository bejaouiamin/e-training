package com.training.formateur.entites;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ActiviteFormateur {
    private String typeActivite;
    private String description;
    private LocalDateTime dateActivite;
    private String utilisateurAction;
}
