package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.ReceitaRequestDTO;
import com.scaglia.financeiro.dto.ReceitaResponseDTO;
import com.scaglia.financeiro.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Receitas", description = "Endpoints para gerenciamento das Receitas financeiras")
@RestController
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    @Operation(summary = "Lista todas as receitas do usuário logado")
    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> listarReceitas() {
        List<ReceitaResponseDTO> receitas = receitaService.listarReceitasUsuario();
        return ResponseEntity.ok(receitas);
    }

    @Operation(summary = "Busca uma receita por ID (pertencente ao usuário)")
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> buscarReceita(@PathVariable Long id) {
        ReceitaResponseDTO receita = receitaService.buscarPorId(id);
        return ResponseEntity.ok(receita);
    }

    @Operation(summary = "Cria uma nova receita")
    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> criarReceita(@Valid @RequestBody ReceitaRequestDTO dto) {
        ReceitaResponseDTO novaReceita = receitaService.criarReceita(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReceita);
    }

    @Operation(summary = "Atualiza uma receita existente")
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> atualizarReceita(@PathVariable Long id, @Valid @RequestBody ReceitaRequestDTO dto) {
        ReceitaResponseDTO receitaAtualizada = receitaService.atualizarReceita(id, dto);
        return ResponseEntity.ok(receitaAtualizada);
    }

    @Operation(summary = "Deleta uma receita por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id) {
        receitaService.deletarReceita(id);
        return ResponseEntity.noContent().build();
    }
}