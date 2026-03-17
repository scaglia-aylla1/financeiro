package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.BalancoResponseDTO;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Calcula o balanço financeiro do usuário para um mês e ano específicos.
     */
    @Transactional(readOnly = true)
    public BalancoResponseDTO calcularBalancoMensal(int mes, int ano) {
        String usuarioId = usuarioAutenticadoService.getUsuarioLogado().getId();

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
        BalancoResponseDTO balanco = BalancoResponseDTO.builder()
                .totalReceitas(totalReceitas)
                .totalDespesas(totalDespesas)
                .balancoFinal(balancoFinal)
                .receitasPorCategoria(receitasPorCategoria)
                .despesasPorCategoria(despesasPorCategoria)
                .build();
        log.info("Balanço mensal calculado. userId={}, ano={}, mes={}, balancoFinal={}",
                usuarioId, ano, mes, balancoFinal);
        return balanco;
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