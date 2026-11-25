package br.edu.graosapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProdutorRequest(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String municipio
) {}
