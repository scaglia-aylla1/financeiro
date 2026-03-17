package com.scaglia.financeiro.service;

import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Responsabilidade única: obter o usuário atualmente autenticado (contexto JWT).
 * Centraliza a lógica que antes estava duplicada em ReceitaService, DespesaService e RelatorioService.
 */
@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoService {

    private final UserRepository userRepository;

    /**
     * Retorna o User completo do usuário logado a partir do contexto de segurança (token JWT).
     *
     * @return usuário autenticado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public User getUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        String emailUsuario = (principal != null && !principal.toString().isBlank())
                ? principal.toString()
                : SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + emailUsuario));
    }
}
