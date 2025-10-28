package com.etraining.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CandidatNotFoundException extends RuntimeException  {

    private final String msg;
}
