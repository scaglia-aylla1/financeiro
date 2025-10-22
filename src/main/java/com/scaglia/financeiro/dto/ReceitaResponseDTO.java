package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class ReceitaResponseDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private NaturezaMovimentacao natureza; // <-- Note este campo
    private CategoriaResponseDTO categoria;
    private String usuarioId;



}