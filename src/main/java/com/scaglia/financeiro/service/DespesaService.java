package com.scaglia.financeiro.service;


import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.dto.DespesaResponseDTO;
import com.scaglia.financeiro.dto.ReceitaRequestDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.model.Despesa;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    // --- Métodos de Suporte e Segurança (Idênticos ao ReceitaService) ---

    private User getUsuarioLogado() {
        // Tenta obter o objeto User completo que foi colocado no contexto pelo SecurityFilter
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        String emailUsuario = (String) principal;

        return userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + emailUsuario));
    }

    private CategoriaResponseDTO toCategoriaSimplesDTO(Categoria categoria) {
        // ... (código idêntico ao ReceitaService)
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setTipo(categoria.getTipo());
        return dto;
    }

    private DespesaResponseDTO toResponseDTO(Despesa despesa) {
        DespesaResponseDTO dto = new DespesaResponseDTO();
        dto.setId(despesa.getId());
        dto.setDescricao(despesa.getDescricao());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());
        dto.setNatureza(despesa.getNatureza());
        dto.setCategoria(toCategoriaSimplesDTO(despesa.getCategoria()));
        dto.setUsuarioId(despesa.getUsuario().getId());
        return dto;
    }

    private void verificarPropriedade(Despesa despesa) {
        if (!despesa.getUsuario().getId().equals(getUsuarioLogado().getId())) {
            throw new RecursoNaoEncontradoException("Despesa", despesa.getId());
        }
    }

    // --- CRUD ---

    // 1. Criar Despesa
    public DespesaResponseDTO criarDespesa(ReceitaRequestDTO dto) { // Usando o DTO de Requisição de Receita

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", dto.getCategoriaId()));

        // **REGRA DE NEGÓCIO CRUCIAL PARA DESPESA:**
        if (categoria.getTipo() != TipoMovimentacao.DESPESA) {
            throw new IllegalArgumentException("Categoria selecionada é do tipo RECEITA. Escolha uma categoria de DESPESA.");
        }

        Despesa despesa = new Despesa();
        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setData(dto.getData());
        despesa.setNatureza(dto.getNatureza());
        despesa.setCategoria(categoria);
        despesa.setUsuario(getUsuarioLogado());

        Despesa savedDespesa = despesaRepository.save(despesa);
        return toResponseDTO(savedDespesa);
    }

    // 2. Listar Despesas do Usuário Logado
    public Page<DespesaResponseDTO> listarDespesasUsuario(
            Long categoriaId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable) {
        String usuarioId = getUsuarioLogado().getId();

        Page<Despesa> despesaPage = despesaRepository.buscarComFiltros(
                usuarioId, categoriaId, dataInicial, dataFinal, pageable
        );

        return despesaPage.map(this::toResponseDTO);
    }

    // 3. Buscar Despesa por ID
    public DespesaResponseDTO buscarPorId(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa", id));
        verificarPropriedade(despesa);
        return toResponseDTO(despesa);
    }

    // 4. Atualizar Despesa
    public DespesaResponseDTO atualizarDespesa(Long id, ReceitaRequestDTO dto) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa", id));
        verificarPropriedade(despesa);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", dto.getCategoriaId()));

        // REGRA DE NEGÓCIO: Verifica se a nova categoria é do tipo DESPESA
        if (categoria.getTipo() != TipoMovimentacao.DESPESA) {
            throw new IllegalArgumentException("Categoria selecionada é do tipo RECEITA. Escolha uma categoria de DESPESA.");
        }

        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setData(dto.getData());
        despesa.setNatureza(dto.getNatureza());
        despesa.setCategoria(categoria);

        Despesa updatedDespesa = despesaRepository.save(despesa);
        return toResponseDTO(updatedDespesa);
    }

    // 5. Deletar Despesa
    public void deletarDespesa(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa", id));
        verificarPropriedade(despesa);
        despesaRepository.delete(despesa);
    }
}
