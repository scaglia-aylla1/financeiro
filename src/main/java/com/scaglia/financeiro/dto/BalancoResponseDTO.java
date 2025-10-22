package com.scaglia.financeiro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class BalancoResponseDTO {

    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal balancoFinal; // totalReceitas - totalDespesas

    // Opcional, mas útil: Resumo de despesas por categoria
    // Chave: Nome da Categoria, Valor: Total da Despesa na Categoria
    private Map<String, BigDecimal> despesasPorCategoria;

    // Opcional: Resumo de receitas por categoria
    private Map<String, BigDecimal> receitasPorCategoria;
}
