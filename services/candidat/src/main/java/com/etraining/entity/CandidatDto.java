package com.etraining.entity;

import com.etraining.Response.CandidatResponse;
import com.etraining.request.CandidatRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CandidatDto {
    private final PasswordEncoder passwordEncoder;

    public CandidatDto(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Candidat ToCandidat(CandidatRequest request){
        if (request == null){
            return null;
        }
        StatutCandidat statut = request.statut() != null ? request.statut() : StatutCandidat.ACTIF;
        LocalDateTime now = LocalDateTime.now();
        return Candidat.builder()
                .id(request.id())
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .dateInscription(request.dateInscription() != null ? request.dateInscription() : now)
                .dateDerniereConnexion(request.dateDerniereConnexion() != null ? request.dateDerniereConnexion() : now)
                .statut(statut)
                .address(request.address())
                .build();
    }

    public CandidatResponse fromCandidat(Candidat candidat){
        if (candidat == null){
            return null;
        }
        return new CandidatResponse(
                candidat.getId(),
                candidat.getFullName(),
                candidat.getEmail(),
                candidat.getPhone(),
                candidat.getPassword(),
                candidat.getDateInscription(),
                candidat.getDateDerniereConnexion(),
                candidat.getAddress(),
                candidat.getStatut(),
                candidat.getPassedQuizCount()
        );
    }
}