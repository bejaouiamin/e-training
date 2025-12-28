package com.etraining.Response;

import com.etraining.entity.Address;
import com.etraining.entity.StatutCandidat;

import java.time.LocalDateTime;

public record CandidatResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String password,
        LocalDateTime dateInscription,
        LocalDateTime dateDerniereConnexion,
        Address address,
        StatutCandidat statut,
        Integer passedQuizCount
) {
}
