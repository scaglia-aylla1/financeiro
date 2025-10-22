package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.config.TokenService;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        if (passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return  ResponseEntity.ok(new ResponseDto(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDto body){
        Optional<User> user = this.repository.findByEmail(body.email());
        if (user.isEmpty()){
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return  ResponseEntity.ok(new ResponseDto(newUser.getName(), token));

        }

        return ResponseEntity.badRequest().build();
    }
}
