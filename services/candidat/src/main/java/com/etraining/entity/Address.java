package com.etraining.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
}
