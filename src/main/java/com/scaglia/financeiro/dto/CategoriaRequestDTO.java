package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
    private String nome;

    @NotNull(message = "O tipo de movimentação é obrigatório (RECEITA ou DESPESA)")
    private TipoMovimentacao tipo;
}