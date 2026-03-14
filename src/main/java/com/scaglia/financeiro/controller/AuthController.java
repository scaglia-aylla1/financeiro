package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.config.TokenService;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody @jakarta.validation.Valid LoginRequestDto body) {
        User user = this.repository.findByEmail(body.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        String token = this.tokenService.generateToken(user);
        return ResponseEntity.ok(new ResponseDto(user.getName(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody @jakarta.validation.Valid RegisterRequestDto body) {
        Optional<User> existingUser = this.repository.findByEmail(body.email());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        this.repository.save(newUser);

        String token = this.tokenService.generateToken(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(newUser.getName(), token));
    }
}
