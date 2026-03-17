package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.TipoMovimentacao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaResponseDTO {
  
    private Long id;
    private String nome;
    private TipoMovimentacao tipo;

    public CategoriaResponseDTO() {
        
    }
}
