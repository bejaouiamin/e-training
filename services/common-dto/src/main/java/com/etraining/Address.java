package com.etraining;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Address {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
}
