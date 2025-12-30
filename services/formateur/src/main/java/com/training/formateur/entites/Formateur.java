package com.training.formateur.entites;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "formateurs")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formateur {
    @Id
    private String id;
    @Indexed(unique = true)
    private String keycloakId;
    private String nom;
    private String prenom;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String telephone;
    private List<String> specialites;
    private List<String> certifications;
    private Integer experienceAnnees;
    private StatutFormateur statut;

    // Disponibilités par jour de la semaine (lundi=1, dimanche=7)
    private Map<Integer, DisponibiliteJour> disponibilites;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateModification = LocalDateTime.now();

    // Historique d'activité
    private List<ActiviteFormateur> historique;


}