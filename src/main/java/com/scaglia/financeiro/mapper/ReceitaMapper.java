package com.scaglia.financeiro.mapper;

import com.scaglia.financeiro.dto.ReceitaResponseDTO;
import com.scaglia.financeiro.model.Receita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceitaMapper {

    private final CategoriaMapper categoriaMapper;

    public ReceitaResponseDTO toResponseDTO(Receita receita) {
        if (receita == null) {
            return null;
        }
        ReceitaResponseDTO dto = new ReceitaResponseDTO();
        dto.setId(receita.getId());
        dto.setDescricao(receita.getDescricao());
        dto.setValor(receita.getValor());
        dto.setData(receita.getData());
        dto.setNatureza(receita.getNatureza());
        dto.setCategoria(categoriaMapper.toResponseDTO(receita.getCategoria()));
        dto.setUsuarioId(receita.getUsuario() != null ? receita.getUsuario().getId() : null);
        return dto;
    }
}
