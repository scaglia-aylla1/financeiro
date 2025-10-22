package com.scaglia.financeiro.repository;

import com.scaglia.financeiro.model.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    // Método customizado para a lógica de deleção da Categoria
    boolean existsByCategoriaId(Long categoriaId);

    Page<Despesa> findAllByUsuarioId(String usuarioId, Pageable pageable);

    // Consulta para somar todas as despesas de um usuário em um determinado mês/ano
    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE d.usuario.id = :usuarioId AND YEAR(d.data) = :ano AND MONTH(d.data) = :mes")
    BigDecimal somarDespesasPorMes(String usuarioId, int mes, int ano);

    // Consulta para agrupar e somar despesas por categoria no mês/ano
    @Query("SELECT d.categoria.nome, COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE d.usuario.id = :usuarioId AND YEAR(d.data) = :ano AND MONTH(d.data) = :mes GROUP BY d.categoria.nome")
    List<Object[]> somarDespesasPorCategoria(String usuarioId, int mes, int ano);

    // NOVO MÉTODO: Paginação e Filtros Dinâmicos
    @Query("SELECT r FROM Despesa r WHERE r.usuario.id = :usuarioId " +
            "AND (:categoriaId IS NULL OR r.categoria.id = :categoriaId) " +
            "AND r.data >= COALESCE(:dataInicial, r.data) " +
            "AND r.data <= COALESCE(:dataFinal, r.data) ")
    Page<Despesa> buscarComFiltros(
            String usuarioId,
            Long categoriaId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable
    );
}
