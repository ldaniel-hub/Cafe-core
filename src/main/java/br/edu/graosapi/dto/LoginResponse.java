package br.edu.graosapi.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {}
