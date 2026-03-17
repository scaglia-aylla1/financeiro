package com.scaglia.financeiro.service;

import com.scaglia.financeiro.config.TokenService;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Responsabilidade: lógica de autenticação (login e registro).
 * O controller apenas delega e devolve a resposta HTTP.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDto login(LoginRequestDto body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        String token = tokenService.generateToken(user);
        return new ResponseDto(user.getName(), token);
    }

    public ResponseDto register(RegisterRequestDto body) {
        if (userRepository.findByEmail(body.email()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);
        return new ResponseDto(newUser.getName(), token);
    }
}
