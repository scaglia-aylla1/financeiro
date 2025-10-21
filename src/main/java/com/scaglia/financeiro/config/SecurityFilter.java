package com.scaglia.financeiro.config;

import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Tenta recuperar o token
        var token = this.recoverToken(request);

        // 2. Só tenta autenticar se um token foi encontrado
        if (token != null) {

            // 3. Valida o token e tenta extrair o login (email)
            var login = this.tokenService.validateToken(token);

            // 4. Se o login for válido, autentica no contexto do Spring Security
            if (login != null) {
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + login)); // Boa prática incluir o login na msg

                // Definição das Autoridades (ROLE_USER é suficiente para este projeto)
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                // Criação do objeto de autenticação (passando o objeto User para o contexto)
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                // Autentica o usuário na requisição atual
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // Se o token for inválido (login == null), o SecurityContextHolder fica vazio.
            // O Spring Security tratará isso na próxima etapa como não autenticado.
        }

        // 5. Continua a cadeia de filtros. ESSENCIAL!
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

}
