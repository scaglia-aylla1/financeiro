package com.scaglia.financeiro.service;

import com.scaglia.financeiro.config.TokenService;
import com.scaglia.financeiro.dto.LoginRequestDto;
import com.scaglia.financeiro.dto.RegisterRequestDto;
import com.scaglia.financeiro.dto.ResponseDto;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
    
        // Retorna os TRÊS argumentos exigidos pelo record
        return new ResponseDto(user.getName(), accessToken, refreshToken);
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

        String accessToken = tokenService.generateToken(newUser);
        String refreshToken = tokenService.generateRefreshToken(newUser);
        
        return new ResponseDto(newUser.getName(), accessToken, refreshToken);
    }

    public ResponseDto regerarTokens(String refreshToken) {
        // 1. Valida o refresh token (extrai o email)
        String email = tokenService.validateToken(refreshToken);
        
        if (email.isEmpty()) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }
    
        // 2. Busca o usuário no banco
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    
        // 3. Gera um NOVO par de tokens (Access e Refresh)
        // Isso é chamado de "Refresh Token Rotation", o que é bem seguro!
        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);
    
        return new ResponseDto(user.getName(), newAccessToken, newRefreshToken);
    }
}
