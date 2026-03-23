package com.scaglia.financeiro.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.scaglia.financeiro.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // Garante que a aplicação não suba sem uma secret configurada adequadamente
    @PostConstruct
    public void validateSecret() {
        if (secret == null || secret.trim().isEmpty() || secret.equals("minha-chave-secreta-padrao-para-dev")) {
            System.err.println("AVISO: A chave secreta JWT não foi configurada corretamente em variáveis de ambiente!");
        }
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("financeiro")
                    .withSubject(user.getEmail())
                    // Dica: Adicione o ID do usuário como uma Claim extra para facilitar buscas no banco depois
                    .withClaim("id", user.getId()) 
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("financeiro")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            // Em vez de apenas null, você poderia logar: "Token inválido ou expirado"
            return ""; 
        }
    }

    private Instant generateExpirationDate() {
        // -03:00 é o padrão de Brasília. Perfeito.
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}