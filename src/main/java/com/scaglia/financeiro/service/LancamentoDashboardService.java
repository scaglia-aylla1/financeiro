package com.scaglia.financeiro.service;

import com.scaglia.financeiro.dto.LancamentoResumoResponseDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.model.Despesa;
import com.scaglia.financeiro.model.Receita;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LancamentoDashboardService {

    private static final int LIMITE_PADRAO = 10;
    private static final int LIMITE_MAXIMO = 50;

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional(readOnly = true)
    public List<LancamentoResumoResponseDTO> listarLancamentosRecentes(Integer limit) {
        int limiteFinal = normalizarLimite(limit);
        String usuarioId = usuarioAutenticadoService.getUsuarioLogado().getId();
        PageRequest pageable = PageRequest.of(
                0,
                limiteFinal,
                Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id"))
        );

        List<Receita> receitas = receitaRepository.findAllByUsuarioId(usuarioId, pageable).getContent();
        List<Despesa> despesas = despesaRepository.findAllByUsuarioId(usuarioId, pageable).getContent();

        return Stream.concat(
                        receitas.stream().map(this::mapearReceita),
                        despesas.stream().map(this::mapearDespesa)
                )
                .sorted(Comparator
                        .comparing(LancamentoResumoResponseDTO::getData).reversed()
                        .thenComparing(LancamentoResumoResponseDTO::getId, Comparator.reverseOrder()))
                .limit(limiteFinal)
                .toList();
    }

    private int normalizarLimite(Integer limit) {
        if (limit == null) {
            return LIMITE_PADRAO;
        }
        if (limit < 1) {
            return 1;
        }
        return Math.min(limit, LIMITE_MAXIMO);
    }

    private LancamentoResumoResponseDTO mapearReceita(Receita receita) {
        return LancamentoResumoResponseDTO.builder()
                .id(receita.getId())
                .descricao(receita.getDescricao())
                .valor(receita.getValor())
                .data(receita.getData())
                .natureza(receita.getNatureza())
                .tipo(TipoMovimentacao.RECEITA)
                .categoriaId(receita.getCategoria().getId())
                .categoriaNome(receita.getCategoria().getNome())
                .build();
    }

    private LancamentoResumoResponseDTO mapearDespesa(Despesa despesa) {
        return LancamentoResumoResponseDTO.builder()
                .id(despesa.getId())
                .descricao(despesa.getDescricao())
                .valor(despesa.getValor())
                .data(despesa.getData())
                .natureza(despesa.getNatureza())
                .tipo(TipoMovimentacao.DESPESA)
                .categoriaId(despesa.getCategoria().getId())
                .categoriaNome(despesa.getCategoria().getNome())
                .build();
    }
}
