package com.scaglia.financeiro.mapper;

import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setTipo(categoria.getTipo());
        return dto;
    }
}
