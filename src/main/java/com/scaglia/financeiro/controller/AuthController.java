package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.ApiResponse;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RefreshTokenRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ResponseDto>> login(@RequestBody @Valid LoginRequestDto body) {
        ResponseDto response = authService.login(body);
        
        // Retorna 200 OK com o envelope
        return ResponseEntity.ok(new ApiResponse<>(response, "Login realizado com sucesso."));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ResponseDto>> register(@RequestBody @Valid RegisterRequestDto body) {
        ResponseDto response = authService.register(body);
        
        // Retorna 201 Created com o envelope
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response, "Usuário registrado com sucesso."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<ResponseDto>> refresh(@RequestBody @Valid RefreshTokenRequestDto body) {
        ResponseDto response = authService.regerarTokens(body.refreshToken());
        return ResponseEntity.ok(new ApiResponse<>(response, "Token renovado com sucesso."));
    }
}
