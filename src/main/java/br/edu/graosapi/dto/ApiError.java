package br.edu.graosapi.dto;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp, int status, String error, String message, Map<String, String> campos) {}
