package com.scaglia.financeiro.service;


import com.scaglia.financeiro.dto.DespesaRequestDTO;
import com.scaglia.financeiro.dto.DespesaResponseDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.mapper.DespesaMapper;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.model.Despesa;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.DespesaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final DespesaMapper despesaMapper;

    private void verificarPropriedade(Despesa despesa) {
        if (!despesa.getUsuario().getId().equals(usuarioAutenticadoService.getUsuarioLogado().getId())) {
            throw new RecursoNaoEncontradoException("Despesa", despesa.getId());
        }
    }

    // --- CRUD ---

    // 1. Criar Despesa
    @Transactional
    public DespesaResponseDTO criarDespesa(DespesaRequestDTO dto) {

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
        despesa.setUsuario(usuarioAutenticadoService.getUsuarioLogado());

        Despesa savedDespesa = despesaRepository.save(despesa);
        log.info("Despesa criada com sucesso. userId={}, despesaId={}", despesa.getUsuario().getId(), savedDespesa.getId());
        return despesaMapper.toResponseDTO(savedDespesa);
    }

    // 2. Listar Despesas do Usuário Logado
    @Transactional(readOnly = true)
    public Page<DespesaResponseDTO> listarDespesasUsuario(
            Long categoriaId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable) {
        String usuarioId = usuarioAutenticadoService.getUsuarioLogado().getId();

        Page<Despesa> despesaPage = despesaRepository.buscarComFiltros(
                usuarioId, categoriaId, dataInicial, dataFinal, pageable
        );

        return despesaPage.map(despesaMapper::toResponseDTO);
    }

    // 3. Buscar Despesa por ID
    @Transactional(readOnly = true)
    public DespesaResponseDTO buscarPorId(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa", id));
        verificarPropriedade(despesa);
        return despesaMapper.toResponseDTO(despesa);
    }

    // 4. Atualizar Despesa
    @Transactional
    public DespesaResponseDTO atualizarDespesa(Long id, DespesaRequestDTO dto) {
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
        log.info("Despesa atualizada com sucesso. userId={}, despesaId={}", despesa.getUsuario().getId(), updatedDespesa.getId());
        return despesaMapper.toResponseDTO(updatedDespesa);
    }

    // 5. Deletar Despesa
    @Transactional
    public void deletarDespesa(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa", id));
        verificarPropriedade(despesa);
        despesaRepository.delete(despesa);
        log.info("Despesa deletada com sucesso. userId={}, despesaId={}", despesa.getUsuario().getId(), despesa.getId());
    }
}
