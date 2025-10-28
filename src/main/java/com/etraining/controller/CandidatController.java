package com.etraining.controller;

import com.etraining.Response.CandidatResponse;
import com.etraining.request.CandidatRequest;
import com.etraining.service.CandidatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidats")
@RequiredArgsConstructor
public class CandidatController {
    private final CandidatService service;


    @PostMapping("/add")
    public ResponseEntity<?> createCandidat(
            @RequestBody @Valid CandidatRequest request
    ) {
        return ResponseEntity.ok(this.service.saveCandidat(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCandidat(
            @RequestBody @Valid CandidatRequest request
    ) {
        this.service.updateCandidat(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CandidatResponse>> findAll() {
        return ResponseEntity.ok(this.service.getAllCandidats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatResponse> findById(
            @PathVariable("id") Long candidateId
    ) {
        return ResponseEntity.ok(this.service.getCandidatById(candidateId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long candidateId
    ) {
        this.service.deleteCandidat(candidateId);
        return ResponseEntity.accepted().build();
    }
}
