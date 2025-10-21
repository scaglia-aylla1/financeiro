package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.TipoMovimentacao;
import lombok.Data;

@Data
public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private TipoMovimentacao tipo;
}
