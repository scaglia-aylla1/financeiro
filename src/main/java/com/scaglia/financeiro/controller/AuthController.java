package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.service.AuthService;
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
    public ResponseEntity<ResponseDto> login(@RequestBody @jakarta.validation.Valid LoginRequestDto body) {
        ResponseDto response = authService.login(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody @jakarta.validation.Valid RegisterRequestDto body) {
        ResponseDto response = authService.register(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
