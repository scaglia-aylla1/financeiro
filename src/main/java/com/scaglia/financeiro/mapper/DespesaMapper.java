package com.scaglia.financeiro.mapper;

import com.scaglia.financeiro.dto.DespesaResponseDTO;
import com.scaglia.financeiro.model.Despesa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DespesaMapper {

    private final CategoriaMapper categoriaMapper;

    public DespesaResponseDTO toResponseDTO(Despesa despesa) {
        if (despesa == null) {
            return null;
        }
        DespesaResponseDTO dto = new DespesaResponseDTO();
        dto.setId(despesa.getId());
        dto.setDescricao(despesa.getDescricao());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());
        dto.setNatureza(despesa.getNatureza());
        dto.setCategoria(categoriaMapper.toResponseDTO(despesa.getCategoria()));
        dto.setUsuarioId(despesa.getUsuario() != null ? despesa.getUsuario().getId() : null);
        return dto;
    }
}
