package com.scaglia.financeiro.dto;


import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReceitaResponseDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private NaturezaMovimentacao natureza;

    // DTO aninhado ou objeto simples para a Categoria
    private CategoriaResponseDTO categoria;

    private String usuarioId;
}
