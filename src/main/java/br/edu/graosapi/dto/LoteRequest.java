package br.edu.graosapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteRequest(
    @NotBlank String codigo,
    @NotBlank String variedade,
    @NotNull Long produtorId
) {}
