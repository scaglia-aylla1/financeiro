package com.scaglia.financeiro.model;

import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição da despesa é obrigatória")
    @Column(name = "descricao", nullable = false, columnDefinition = "VARCHAR(255)")
    private String descricao;

    @NotNull(message = "O valor da despesa é obrigatório")
    // Valor pode ser 0.01, mas nunca negativo
    @DecimalMin(value = "0.01", message = "O valor deve ser positivo")
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull(message = "A data da despesa é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @NotNull(message = "A natureza da movimentação é obrigatória (FIXA ou VARIAVEL)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NaturezaMovimentacao natureza; // FIXA ou VARIAVEL

    // Relacionamento com Categoria (Muitas Despesas para Uma Categoria)
    @NotNull(message = "A categoria da despesa é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User usuario;

}