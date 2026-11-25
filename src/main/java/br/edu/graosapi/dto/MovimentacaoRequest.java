package br.edu.graosapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MovimentacaoRequest(
    @NotNull TipoMovimentacao tipo,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal quantidadeKg
) {}
