package com.scaglia.financeiro.repository;


import com.scaglia.financeiro.model.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    // Método customizado para a lógica de deleção da Categoria
    boolean existsByCategoriaId(Long categoriaId);

    Page<Receita> findAllByUsuarioId(String usuarioId, Pageable pageable);

    // Consulta para somar todas as receitas de um usuário em um determinado mês/ano
    @Query("SELECT COALESCE(SUM(r.valor), 0) FROM Receita r WHERE r.usuario.id = :usuarioId AND YEAR(r.data) = :ano AND MONTH(r.data) = :mes")
    BigDecimal somarReceitasPorMes(String usuarioId, int mes, int ano);

    // Consulta para agrupar e somar receitas por categoria no mês/ano
    @Query("SELECT r.categoria.nome, COALESCE(SUM(r.valor), 0) FROM Receita r WHERE r.usuario.id = :usuarioId AND YEAR(r.data) = :ano AND MONTH(r.data) = :mes GROUP BY r.categoria.nome")
    List<Object[]> somarReceitasPorCategoria(String usuarioId, int mes, int ano);

    // NOVO MÉTODO: Paginação e Filtros Dinâmicos
    @Query("SELECT r FROM Receita r WHERE r.usuario.id = :usuarioId " +
            "AND (:categoriaId IS NULL OR r.categoria.id = :categoriaId) " +
            "AND r.data >= COALESCE(:dataInicial, r.data) " +
            "AND r.data <= COALESCE(:dataFinal, r.data) ")
    Page<Receita> buscarComFiltros(
            String usuarioId,
            Long categoriaId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable
    );
}