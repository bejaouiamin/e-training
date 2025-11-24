package com.training.formateur.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class FormateurNotFoundException extends RuntimeException {
    private final String msg;
}

