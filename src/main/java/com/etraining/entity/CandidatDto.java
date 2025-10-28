package com.etraining.entity;

import com.etraining.Response.CandidatResponse;
import com.etraining.request.CandidatRequest;
import org.springframework.stereotype.Component;

@Component
public class CandidatDto {

    public Candidat ToCandidat(CandidatRequest request){
        if (request == null){
            return null;
        }
        return Candidat.builder()
                .id(request.id())
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
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
                candidat.getStatut()
        );
    }
}
