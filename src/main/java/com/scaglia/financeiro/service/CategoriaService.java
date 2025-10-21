package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.CategoriaRequestDTO;
import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;


    // Converte Model para Response DTO
    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setTipo(categoria.getTipo());
        return dto;
    }

    // 1. Criar Categoria
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO dto) {

        // Regra de Negócio: Garante que o nome da categoria é único (ignorando caixa)
        if (categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome '" + dto.getNome() + "'.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setTipo(dto.getTipo());

        Categoria savedCategoria = categoriaRepository.save(categoria);
        return toResponseDTO(savedCategoria);
    }

    // 2. Buscar Categoria por ID
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
        return toResponseDTO(categoria);
    }

    // 3. Listar Todas as Categorias
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 4. Atualizar Categoria
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));

        // Validação de nome único (excluindo a categoria atual)
        if (!categoria.getNome().equalsIgnoreCase(dto.getNome()) && categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new IllegalArgumentException("Já existe outra categoria com o nome '" + dto.getNome() + "'.");
        }

        categoria.setNome(dto.getNome());
        categoria.setTipo(dto.getTipo());

        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return toResponseDTO(updatedCategoria);
    }

    public void deletarCategoria(Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));

        // LÓGICA PROFISSIONAL: Verifica se a categoria está sendo usada por qualquer movimentação
        if (receitaRepository.existsByCategoriaId(id) || despesaRepository.existsByCategoriaId(id)) {
            // Se estiver em uso, lança exceção (tratada pelo GlobalExceptionHandler como 400 Bad Request)
            throw new IllegalArgumentException("Não é possível excluir a categoria '" + categoria.getNome() +
                    "' pois ela está em uso por movimentações financeiras. Remova os vínculos primeiro.");
        }

        // Se não estiver em uso, a categoria pode ser deletada.
        categoriaRepository.delete(categoria);
    }
}
