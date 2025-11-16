package com.training.formateur.handler;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
