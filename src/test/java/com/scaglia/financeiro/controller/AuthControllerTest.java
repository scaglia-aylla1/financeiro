package com.scaglia.financeiro.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RefreshTokenRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")// Configura o MockMvc para simular chamadas HTTP
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService; // "Mocamos" o service para focar no Controller

    @Test
    void login_DeveRetornar200_QuandoCredenciaisForemValidas() throws Exception {
        // GIVEN
        LoginRequestDto loginRequest = new LoginRequestDto("usuario@email.com", "senha123");
        
        // AJUSTE: Adicionado o terceiro parâmetro (refresh token) para bater com o novo ResponseDto
        ResponseDto responseFake = new ResponseDto(
                "nomeUsuario", 
                "access-token-fake", 
                "refresh-token-fake"
        );

        when(authService.login(any(LoginRequestDto.class))).thenReturn(responseFake);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) 
                // AJUSTE: Nomes dos campos devem bater com o seu Record ResponseDto
                .andExpect(jsonPath("$.data.accessToken").value("access-token-fake")) 
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token-fake"))
                .andExpect(jsonPath("$.data.name").value("nomeUsuario"))
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso."));
    }

    @Test
    void refresh_DeveRetornarNovosTokens_QuandoRefreshTokenForValido() throws Exception {
        // GIVEN
        RefreshTokenRequestDto refreshRequest = new RefreshTokenRequestDto("refresh-valido");
        ResponseDto novosTokens = new ResponseDto("nomeUsuario", "novo-access", "novo-refresh");

        when(authService.regerarTokens("refresh-valido")).thenReturn(novosTokens);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("novo-access"))
                .andExpect(jsonPath("$.data.refreshToken").value("novo-refresh"))
                .andExpect(jsonPath("$.message").value("Token renovado com sucesso."));
    }

    @Test
    void login_DeveRetornar400EDetalhesDoErro_QuandoEmailForInvalido() throws Exception {
    // GIVEN: Email com formato errado e senha vazia
    LoginRequestDto loginInvalido = new LoginRequestDto("email-errado", ""); 

    // WHEN & THEN
    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginInvalido)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR")) // Valida o campo do seu ErrorDetails
            .andExpect(jsonPath("$.message").value("Erro de Validação"));
}
}