package com.scaglia.financeiro.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scaglia.financeiro.exception.RecursoNaoEncontradoException;
import com.scaglia.financeiro.mapper.ReceitaMapper;
import com.scaglia.financeiro.model.Receita;
import com.scaglia.financeiro.model.User;
import com.scaglia.financeiro.repository.CategoriaRepository;
import com.scaglia.financeiro.repository.ReceitaRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTest {

    @Mock
    private ReceitaRepository receitaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;
    @Mock
    private ReceitaMapper receitaMapper;

    @InjectMocks
    private ReceitaService service;

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoUsuarioNaoForDono() {
        // GIVEN: Criamos dois usuários diferentes
        User dono = new User();
        dono.setId("usuario-logado-123");

        User invasor = new User();
        invasor.setId("usuario-hacker-456");

        Receita receitaDoBanco = new Receita();
        receitaDoBanco.setId(1L);
        receitaDoBanco.setUsuario(dono); // A receita pertence ao 'dono'

        // Ensinamos o Mockito
        when(receitaRepository.findById(1L)).thenReturn(Optional.of(receitaDoBanco));
        
        // Simulamos que quem está logado AGORA é o 'invasor'
        when(usuarioAutenticadoService.getUsuarioLogado()).thenReturn(invasor);

        // WHEN & THEN: O teste espera que uma exceção aconteça
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.buscarPorId(1L);
        });

        // Verificamos que o mapper NUNCA foi chamado (o dado não vazou)
        verify(receitaMapper, never()).toResponseDTO(any());
    }
}