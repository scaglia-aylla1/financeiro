package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.DespesaResponseDTO;
import com.scaglia.financeiro.dto.ReceitaRequestDTO;
import com.scaglia.financeiro.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Despesas", description = "Endpoints para gerenciamento das Despesas financeiras")
@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @Operation(summary = "Lista todas as despesas do usuário logado")
    @GetMapping
    public ResponseEntity<List<DespesaResponseDTO>> listarDespesas() {
        List<DespesaResponseDTO> despesas = despesaService.listarDespesasUsuario();
        return ResponseEntity.ok(despesas);
    }

    @Operation(summary = "Busca uma despesa por ID (pertencente ao usuário)")
    @GetMapping("/{id}")
    public ResponseEntity<DespesaResponseDTO> buscarDespesa(@PathVariable Long id) {
        DespesaResponseDTO despesa = despesaService.buscarPorId(id);
        return ResponseEntity.ok(despesa);
    }

    @Operation(summary = "Cria uma nova despesa")
    @PostMapping
    public ResponseEntity<DespesaResponseDTO> criarDespesa(@Valid @RequestBody ReceitaRequestDTO dto) {
        DespesaResponseDTO novaDespesa = despesaService.criarDespesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDespesa);
    }

    @Operation(summary = "Atualiza uma despesa existente")
    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponseDTO> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody ReceitaRequestDTO dto) {
        DespesaResponseDTO despesaAtualizada = despesaService.atualizarDespesa(id, dto);
        return ResponseEntity.ok(despesaAtualizada);
    }

    @Operation(summary = "Deleta uma despesa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable Long id) {
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }
}
