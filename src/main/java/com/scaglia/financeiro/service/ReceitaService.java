package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.ReceitaRequestDTO;
import com.scaglia.financeiro.dto.ReceitaResponseDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.mapper.ReceitaMapper;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.model.Receita;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
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
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final ReceitaMapper receitaMapper;

    /**
     * Verifica se a receita pertence ao usuário logado. Essencial para segurança.
     */
    private void verificarPropriedade(Receita receita) {
        if (!receita.getUsuario().getId().equals(usuarioAutenticadoService.getUsuarioLogado().getId())) {
            // Retorna 404 para esconder a existência do recurso de um usuário não-proprietário.
            throw new RecursoNaoEncontradoException("Receita", receita.getId());
        }
    }


    // --- CRUD ---

    /**
     * 1. Cria uma nova Receita, validando a categoria e associando ao usuário logado.
     */
    @Transactional
    public ReceitaResponseDTO criarReceita(ReceitaRequestDTO dto) {

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", dto.getCategoriaId()));

        // REGRA DE NEGÓCIO: Garante que a categoria é de RECEITA
        if (categoria.getTipo() != TipoMovimentacao.RECEITA) {
            throw new IllegalArgumentException("Categoria selecionada é do tipo DESPESA. Escolha uma categoria de RECEITA.");
        }

        Receita receita = new Receita();
        receita.setDescricao(dto.getDescricao());
        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receita.setNatureza(dto.getNatureza());
        receita.setCategoria(categoria);

        // ASSOCIAÇÃO SEGURA
        receita.setUsuario(usuarioAutenticadoService.getUsuarioLogado());

        Receita savedReceita = receitaRepository.save(receita);
        log.info("Receita criada com sucesso. userId={}, receitaId={}", receita.getUsuario().getId(), savedReceita.getId());
        return receitaMapper.toResponseDTO(savedReceita);
    }

    /**
     * 2. Listar Receitas do Usuário Logado com Filtros e Paginação.
     */
    @Transactional(readOnly = true)
    public Page<ReceitaResponseDTO> listarReceitasUsuario(
            Long categoriaId,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable) {

        String usuarioId = usuarioAutenticadoService.getUsuarioLogado().getId();

        // **MUDANÇA CRUCIAL:** Novo método no Repository para aceitar os filtros
        Page<Receita> receitasPage = receitaRepository.buscarComFiltros(
                usuarioId, categoriaId, dataInicial, dataFinal, pageable
        );

        return receitasPage.map(receitaMapper::toResponseDTO);
    }

    /**
     * 3. Busca Receita por ID e verifica a propriedade.
     */
    @Transactional(readOnly = true)
    public ReceitaResponseDTO buscarPorId(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita", id));

        // SEGURANÇA: Checa se o usuário logado é o proprietário
        verificarPropriedade(receita);

        return receitaMapper.toResponseDTO(receita);
    }

    /**
     * 4. Atualiza uma Receita, verificando a propriedade e a categoria.
     */
    @Transactional
    public ReceitaResponseDTO atualizarReceita(Long id, ReceitaRequestDTO dto) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita", id));

        // SEGURANÇA: Checa se o usuário logado é o proprietário
        verificarPropriedade(receita);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", dto.getCategoriaId()));

        // REGRA DE NEGÓCIO: Verifica se a nova categoria é do tipo RECEITA
        if (categoria.getTipo() != TipoMovimentacao.RECEITA) {
            throw new IllegalArgumentException("Categoria selecionada é do tipo DESPESA. Escolha uma categoria de RECEITA.");
        }

        receita.setDescricao(dto.getDescricao());
        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receita.setNatureza(dto.getNatureza());
        receita.setCategoria(categoria);

        Receita updatedReceita = receitaRepository.save(receita);
        log.info("Receita atualizada com sucesso. userId={}, receitaId={}", receita.getUsuario().getId(), updatedReceita.getId());
        return receitaMapper.toResponseDTO(updatedReceita);
    }

    /**
     * 5. Deleta uma Receita, verificando a propriedade.
     */
    @Transactional
    public void deletarReceita(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita", id));

        // SEGURANÇA: Checa se o usuário logado é o proprietário
        verificarPropriedade(receita);

        receitaRepository.delete(receita);
        log.info("Receita deletada com sucesso. userId={}, receitaId={}", receita.getUsuario().getId(), receita.getId());
    }
}