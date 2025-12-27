package com.etraining.service;

import com.etraining.Response.CandidatResponse;
import org.apache.commons.lang.StringUtils;
import com.etraining.entity.Candidat;
import com.etraining.entity.CandidatDto;
import com.etraining.exception.CandidatNotFoundException;
import com.etraining.repository.CandidatRepository;
import com.etraining.request.CandidatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatService {
    private final CandidatRepository repository;
    private final CandidatDto candidatDto;


    public Long saveCandidat(CandidatRequest candidatRequest){
        var candidat = this.repository.save(candidatDto.ToCandidat(candidatRequest));
        return candidat.getId();
    }

    public void updateCandidat(Long id, CandidatRequest request) {
        var candidat = this.repository.findById(id).orElseThrow(() ->
                new CandidatNotFoundException(
                        String.format("Cannot update candidat :: No candidat found with the provided Id: %s", id)
                ));
        mergeCandidat(candidat, request);
        this.repository.save(candidat);
    }

    public void mergeCandidat(Candidat candidat, CandidatRequest request) {
        if (StringUtils.isNotBlank(request.fullName())) {
            candidat.setFullName(request.fullName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            candidat.setEmail(request.email());
        }
        if (StringUtils.isNotBlank(request.phone())) {
            candidat.setPhone(request.phone());
        }
        if (StringUtils.isNotBlank(request.password())) {
            candidat.setPassword(request.password());
        }
        if (request.address() != null) {
            candidat.setAddress(request.address());
        }
        if (request.dateInscription() != null) {
            candidat.setDateInscription(request.dateInscription());
        }
        if (request.dateDerniereConnexion() != null) {
            candidat.setDateDerniereConnexion(request.dateDerniereConnexion());
        }
        if (request.statut() != null) {
            candidat.setStatut(request.statut());
        }
    }

    public List<CandidatResponse> getAllCandidats(){
        return this.repository.findAll()
                .stream()
                .map(candidatDto::fromCandidat)
                .collect(Collectors.toList());
    }

    public CandidatResponse getCandidatById(Long id){
        return this.repository.findById(id)
                .map(candidatDto::fromCandidat)
                .orElseThrow(()->
                        new CandidatNotFoundException(
                                String.format("No candidat found with the provided Id:%s",id)
                        ));
    }

    public void deleteCandidat(Long id){
        this.repository.deleteById(id);
    }

    @Transactional
    public void handleQuizSubmitted(Long candidateId, Long quizResourceId, Integer score, boolean passed, Instant submittedAt) {
        repository.findById(candidateId).ifPresent(c -> {
            // exemple : incrémente un compteur de quizzes validés si passed == true
            if (passed) {
                Integer v = c.getPassedQuizCount() == null ? 0 : c.getPassedQuizCount();
                c.setPassedQuizCount(v + 1);
            }
            // enregistrer éventuellement la tentative dans une table dédiée si vous avez une entité
            repository.save(c);
        });
        // log ou métrique si candidat introuvable
    }

}
