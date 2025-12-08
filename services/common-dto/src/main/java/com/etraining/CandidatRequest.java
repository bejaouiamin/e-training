package com.etraining;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record  CandidatRequest(

        Long id,
        @NotNull(message = " fullName is required")
        String fullName,

        @NotNull(message = " Email is required")
        @Email(message = " Email is not a valid email address")
        String email,
        @NotNull(message = " phone is required")
        @Pattern(regexp = "\\d{8}", message = "the phone must have 8 numbers")
        String phone,

        @NotNull(message = " password is required")
        String password,

        LocalDateTime dateInscription,
        LocalDateTime dateDerniereConnexion,

        Address address,

        StatutCandidat statut
)
{ }