package com.scaglia.financeiro.repository;


import com.scaglia.financeiro.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    // Método customizado para a lógica de deleção da Categoria
    boolean existsByCategoriaId(Long categoriaId);

    List<Receita> findAllByUsuarioId(String usuarioId);


    // Método customizado para o Dashboard (obter todas as receitas de um usuário)
    // O Spring Security garantirá que apenas o usuário logado acesse.
    // Vamos adicionar métodos de consulta para o Dashboard mais tarde.
}