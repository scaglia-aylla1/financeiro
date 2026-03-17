package com.scaglia.financeiro.repository;


import static org.junit.jupiter.api.Assertions.*;


import com.scaglia.financeiro.enums.NaturezaMovimentacao;
import com.scaglia.financeiro.enums.TipoMovimentacao;
import com.scaglia.financeiro.model.Categoria;
import com.scaglia.financeiro.model.Receita;
import com.scaglia.financeiro.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@DataJpaTest // Configura um banco H2 automaticamente
@ActiveProfiles("test") // Indica para usar o application-test.properties
class ReceitaRepositoryTest {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private TestEntityManager entityManager; // Ajuda a preparar os dados no banco

    @Test
    void buscarComFiltros_DeveRetornarReceitas_DentroDoPeriodoInformado() {
        Instant agora = Instant.now();
        String autor = "sistema";

        // 1. GIVEN - USUÁRIO
        User usuario = new User();
        usuario.setName("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setPassword("123456");
        usuario.setCreatedAt(agora);
        usuario.setUpdatedAt(agora);
        usuario.setCreatedBy(autor);
        usuario.setUpdatedBy(autor);
        usuario = entityManager.persistFlushFind(usuario);

        // 2. GIVEN - CATEGORIA
        Categoria cat = new Categoria();
        cat.setNome("Salário");
        cat.setTipo(TipoMovimentacao.RECEITA);
        cat.setCreatedAt(agora);
        cat.setUpdatedAt(agora);
        cat.setCreatedBy(autor);
        cat.setUpdatedBy(autor);
        cat = entityManager.persistFlushFind(cat);

        // 3. GIVEN - RECEITA
        Receita r1 = new Receita();
        r1.setDescricao("Freelance");
        r1.setValor(new BigDecimal("1000.00"));
        r1.setData(LocalDate.of(2023, 10, 1));
        r1.setNatureza(NaturezaMovimentacao.VARIAVEL);
        r1.setCategoria(cat);
        r1.setUsuario(usuario);
        // Campos de auditoria da Receita também!
        r1.setCreatedAt(agora);
        r1.setUpdatedAt(agora);
        r1.setCreatedBy(autor);
        r1.setUpdatedBy(autor);
        
        entityManager.persist(r1);
        entityManager.flush(); 

        // WHEN
        Page<Receita> resultado = receitaRepository.buscarComFiltros(
            usuario.getId(), null, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 31), PageRequest.of(0, 10)
        );

        // THEN
        assertEquals(1, resultado.getTotalElements());
    }
}
