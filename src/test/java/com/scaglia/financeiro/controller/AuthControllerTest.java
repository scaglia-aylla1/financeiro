package com.scaglia.financeiro.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaglia.financeiro.dto.LoginRequestDto;
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
        ResponseDto responseFake = new ResponseDto("nomeUsuario", "token-jwt-gerado-aqui");

        // Ensinamos o service a retornar o DTO fake
        when(authService.login(any(LoginRequestDto.class))).thenReturn(responseFake);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON) // Dizemos que estamos enviando JSON
                .content(objectMapper.writeValueAsString(loginRequest))) // Converte objeto para JSON string
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(jsonPath("$.token").value("token-jwt-gerado-aqui")) // Verifica se o token está no JSON
                .andExpect(jsonPath("$.name").value("nomeUsuario"));
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