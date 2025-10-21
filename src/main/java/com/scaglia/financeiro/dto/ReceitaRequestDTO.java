package com.scaglia.financeiro.dto;

import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReceitaRequestDTO {

    @NotBlank(message = "A descrição da receita é obrigatória")
    private String descricao;

    @NotNull(message = "O valor da receita é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "A data da receita é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull(message = "A natureza da movimentação é obrigatória (FIXA ou VARIAVEL)")
    private NaturezaMovimentacao natureza;

    @NotNull(message = "O ID da categoria é obrigatório")
    @Positive(message = "O ID da categoria deve ser um número positivo")
    private Long categoriaId;
}
