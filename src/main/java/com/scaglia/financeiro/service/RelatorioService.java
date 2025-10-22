package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.BalancoResponseDTO;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final UserRepository userRepository;

    private User getUsuarioLogado() {
        // Lógica de obtenção do User do contexto (idêntica ao ReceitaService)
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + emailUsuario));
    }

    /**
     * Calcula o balanço financeiro do usuário para um mês e ano específicos.
     */
    public BalancoResponseDTO calcularBalancoMensal(int mes, int ano) {
        String usuarioId = getUsuarioLogado().getId();

        // 1. Soma Total de Receitas
        BigDecimal totalReceitas = receitaRepository.somarReceitasPorMes(usuarioId, mes, ano);

        // 2. Soma Total de Despesas
        BigDecimal totalDespesas = despesaRepository.somarDespesasPorMes(usuarioId, mes, ano);

        // 3. Cálculo do Balanço Final
        BigDecimal balancoFinal = totalReceitas.subtract(totalDespesas);

        // 4. Detalhamento por Categoria
        Map<String, BigDecimal> receitasPorCategoria = converterListaDeSomas(
                receitaRepository.somarReceitasPorCategoria(usuarioId, mes, ano)
        );

        Map<String, BigDecimal> despesasPorCategoria = converterListaDeSomas(
                despesaRepository.somarDespesasPorCategoria(usuarioId, mes, ano)
        );

        // 5. Constrói o DTO de Resposta
        return BalancoResponseDTO.builder()
                .totalReceitas(totalReceitas)
                .totalDespesas(totalDespesas)
                .balancoFinal(balancoFinal)
                .receitasPorCategoria(receitasPorCategoria)
                .despesasPorCategoria(despesasPorCategoria)
                .build();
    }

    /**
     * Converte a List<Object[]> retornada pelo JPA em um Map<String, BigDecimal>.
     * O formato da lista é: [[nomeCategoria, somaValor], [nomeCategoria, somaValor], ...]
     */
    private Map<String, BigDecimal> converterListaDeSomas(List<Object[]> somas) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        for (Object[] item : somas) {
            String nome = (String) item[0];
            BigDecimal valor = (BigDecimal) item[1];
            resultado.put(nome, valor);
        }
        return resultado;
    }
}