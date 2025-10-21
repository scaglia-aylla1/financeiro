package com.scaglia.financeiro.repository;

import com.scaglia.financeiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Método de consulta customizado
    // Usado para garantir que não há categorias duplicadas com o mesmo nome
    boolean existsByNomeIgnoreCase(String nome);
}
