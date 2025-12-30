package com.etraining.controller;

import com.etraining.Response.CandidatResponse;
import com.etraining.entity.Candidat;
import com.etraining.exception.CandidatNotFoundException;
import com.etraining.repository.CandidatRepository;
import com.etraining.request.CandidatRequest;
import com.etraining.service.CandidatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidats")
@RequiredArgsConstructor
public class CandidatController {
    private final CandidatService service;
    private final CandidatRepository candidatRepository;

    @PostMapping("/add")
    public ResponseEntity<CandidatRequest> createCandidat(
            @RequestBody @Valid CandidatRequest request
    ) {
        this.service.saveCandidat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCandidat(
            @PathVariable("id") Long id,
            @RequestBody @Valid CandidatRequest request
    ) {
        try {
            this.service.updateCandidat(id, request);
            return ResponseEntity.ok("candidat updated successfully");
        } catch (CandidatNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMsg());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<CandidatResponse>> findAll() {
        return ResponseEntity.ok(this.service.getAllCandidats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @PathVariable("id") Long candidateId
    ) {
        try {
            CandidatResponse candidat = this.service.getCandidatById(candidateId);
            return ResponseEntity.ok(candidat);
        } catch (CandidatNotFoundException e) {
            return ResponseEntity.status(404).body("Candidate not found with ID: " + candidateId);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") Long candidateId
    ) {
        this.service.deleteCandidat(candidateId);
        return ResponseEntity.ok("candidat deleted successfully");
    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<Candidat> getCandidatByKeycloakId(@PathVariable String keycloakId) {
        return candidatRepository.findByKeycloakId(keycloakId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
