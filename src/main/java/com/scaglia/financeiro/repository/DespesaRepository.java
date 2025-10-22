package com.scaglia.financeiro.repository;

import com.scaglia.financeiro.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    // Método customizado para a lógica de deleção da Categoria
    boolean existsByCategoriaId(Long categoriaId);

    List<Despesa> findAllByUsuarioId(String usuarioId);

    // Método customizado para o Dashboard (obter todas as despesas de um usuário)
}
