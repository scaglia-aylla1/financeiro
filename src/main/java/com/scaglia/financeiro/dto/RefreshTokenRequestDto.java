package com.scaglia.financeiro.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
    @NotBlank(message = "O refresh token é obrigatório")
    String refreshToken
) {
}