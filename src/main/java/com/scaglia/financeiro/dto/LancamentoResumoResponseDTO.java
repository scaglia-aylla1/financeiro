package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoResumoResponseDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private NaturezaMovimentacao natureza;
    private TipoMovimentacao tipo;
    private Long categoriaId;
    private String categoriaNome;
}
