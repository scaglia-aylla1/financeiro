package com.scaglia.financeiro.model;

import com.scaglia.financeiro.enums.TipoMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Setter
@Getter
@NoArgsConstructor  
@AllArgsConstructor
public class Categoria extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "O tipo de movimentação é obrigatório (RECEITA ou DESPESA)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo; // RECEITA ou DESPESA

}
