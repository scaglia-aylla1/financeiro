package com.scaglia.financeiro.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scaglia.financeiro.dto.CategoriaRequestDTO;
import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.mapper.CategoriaMapper;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.DespesaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;

import java.util.Optional;

// Importe suas classes de DTO, Entity e Exception aqui

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    // Adicione os outros mocks que o construtor do Service exige
    @Mock
    private ReceitaRepository receitaRepository;
    @Mock
    private DespesaRepository despesaRepository;

    @InjectMocks
    private CategoriaService service;

    @Test
void criarCategoria_DeveRetornarSucesso_QuandoNomeNaoExiste() {
    // 1. GIVEN - O erro deve estar aqui: o DTO precisa ter o nome!
    CategoriaRequestDTO request = new CategoriaRequestDTO();
    request.setNome("Alimentação"); // CERTIFIQUE-SE DE SETAR O NOME AQUI
    request.setTipo(TipoMovimentacao.DESPESA);

    Categoria categoriaSalva = new Categoria(1L, "Alimentação", TipoMovimentacao.DESPESA);
    CategoriaResponseDTO responseEsperado = new CategoriaResponseDTO(1L, "Alimentação", TipoMovimentacao.DESPESA);

    // Stubbing: O valor aqui deve ser IGUAL ao que está no request.setNome() acima
    when(categoriaRepository.existsByNomeIgnoreCase("Alimentação")).thenReturn(false);
    
    when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);
    when(categoriaMapper.toResponseDTO(categoriaSalva)).thenReturn(responseEsperado);

    // 2. WHEN
    CategoriaResponseDTO resultado = service.criarCategoria(request);

    // 3. THEN
    assertNotNull(resultado);
    assertEquals("Alimentação", resultado.getNome());
}
@Test
void criarCategoria_DeveLancarExcecao_QuandoNomeJaExiste() {
    // 1. GIVEN
    CategoriaRequestDTO request = new CategoriaRequestDTO();
    request.setNome("Lazer"); // VOCÊ PRECISA DESTA LINHA!
    // request.setTipo(TipoMovimentacao.DESPESA); // Opcional para este teste, mas bom ter

    // Agora o Mockito vai reconhecer o "Lazer" vindo do service
    when(categoriaRepository.existsByNomeIgnoreCase("Lazer")).thenReturn(true);

    // 2. WHEN & THEN
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        service.criarCategoria(request);
    });

    assertEquals("Já existe uma categoria com o nome 'Lazer'.", exception.getMessage());
    verify(categoriaRepository, never()).save(any());
}
}
