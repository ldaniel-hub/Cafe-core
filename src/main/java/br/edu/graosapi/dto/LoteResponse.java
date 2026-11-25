package br.edu.graosapi.dto;

import java.math.BigDecimal;

public record LoteResponse(Long id, String codigo, String variedade, BigDecimal quantidadeKg, Long produtorId) {}
