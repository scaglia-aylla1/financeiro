package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.dto.ReceitaRequestDTO;
import com.scaglia.financeiro.dto.ReceitaResponseDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.model.Receita;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
import com.scaglia.financeiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    // --- Métodos de Suporte e Segurança ---

    /**
     * Obtém o objeto User completo a partir do contexto de segurança (token JWT).
     * Garante que a transação será associada ao usuário correto.
     */
    // No ReceitaService.java
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
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setTipo(categoria.getTipo());
        return dto;
    }

    /**
     * Converte a Entidade Receita para o DTO de Resposta, incluindo o ID do usuário (String).
     */
    private ReceitaResponseDTO toResponseDTO(Receita receita) {
        ReceitaResponseDTO dto = new ReceitaResponseDTO();
        dto.setId(receita.getId());
        dto.setDescricao(receita.getDescricao());
        dto.setValor(receita.getValor());
        dto.setData(receita.getData());
        dto.setNatureza(receita.getNatureza());
        dto.setCategoria(toCategoriaSimplesDTO(receita.getCategoria()));

        // CORREÇÃO: Usando String para o ID do Usuário (UUID)
        dto.setUsuarioId(receita.getUsuario().getId());

        return dto;
    }

    /**
     * Verifica se a receita pertence ao usuário logado. Essencial para segurança.
     */
    private void verificarPropriedade(Receita receita) {
        if (!receita.getUsuario().getId().equals(getUsuarioLogado().getId())) {
            // Retorna 404 para esconder a existência do recurso de um usuário não-proprietário.
            throw new RecursoNaoEncontradoException("Receita", receita.getId());
        }
    }

    // --- CRUD ---

    /**
     * 1. Cria uma nova Receita, validando a categoria e associando ao usuário logado.
     */
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
        receita.setUsuario(getUsuarioLogado());

        Receita savedReceita = receitaRepository.save(receita);
        return toResponseDTO(savedReceita);
    }

    /**
     * 2. Lista todas as Receitas pertencentes ao usuário logado.
     */
    public List<ReceitaResponseDTO> listarReceitasUsuario() {
        String usuarioId = getUsuarioLogado().getId();

        // CORRETO: Buscar apenas as receitas do usuário logado.
        // **NOTA:** Este método (findAllByUsuarioId) deve ser criado no ReceitaRepository.
        return receitaRepository.findAllByUsuarioId(usuarioId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 3. Busca Receita por ID e verifica a propriedade.
     */
    public ReceitaResponseDTO buscarPorId(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita", id));

        // SEGURANÇA: Checa se o usuário logado é o proprietário
        verificarPropriedade(receita);

        return toResponseDTO(receita);
    }

    /**
     * 4. Atualiza uma Receita, verificando a propriedade e a categoria.
     */
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
        return toResponseDTO(updatedReceita);
    }

    /**
     * 5. Deleta uma Receita, verificando a propriedade.
     */
    public void deletarReceita(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita", id));

        // SEGURANÇA: Checa se o usuário logado é o proprietário
        verificarPropriedade(receita);

        receitaRepository.delete(receita);
    }
}