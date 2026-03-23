package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.ApiResponse; // Importe seu envelope
import com.scaglia.financeiro.dto.DespesaRequestDTO;
import com.scaglia.financeiro.dto.DespesaResponseDTO;
import com.scaglia.financeiro.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Despesas", description = "Endpoints para gerenciamento das Despesas financeiras")
@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @Operation(summary = "Lista despesas do usuário com filtros, paginação e ordenação")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<DespesaResponseDTO>>> listarDespesas(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @PageableDefault(size = 10, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<DespesaResponseDTO> despesas = despesaService.listarDespesasUsuario(
                categoriaId, dataInicial, dataFinal, pageable
        );
        return ResponseEntity.ok(new ApiResponse<>(despesas, "Despesas listadas com sucesso."));
    }

    @Operation(summary = "Busca uma despesa por ID (pertencente ao usuário)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DespesaResponseDTO>> buscarDespesa(@PathVariable Long id) {
        DespesaResponseDTO despesa = despesaService.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(despesa, "Despesa encontrada."));
    }

    @Operation(summary = "Cria uma nova despesa")
    @PostMapping
    public ResponseEntity<ApiResponse<DespesaResponseDTO>> criarDespesa(@Valid @RequestBody DespesaRequestDTO dto) {
        DespesaResponseDTO novaDespesa = despesaService.criarDespesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(novaDespesa, "Despesa registrada com sucesso."));
    }

    @Operation(summary = "Atualiza uma despesa existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DespesaResponseDTO>> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody DespesaRequestDTO dto) {
        DespesaResponseDTO despesaAtualizada = despesaService.atualizarDespesa(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(despesaAtualizada, "Despesa atualizada com sucesso."));
    }

    @Operation(summary = "Deleta uma despesa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarDespesa(@PathVariable Long id) {
        despesaService.deletarDespesa(id);
        // Alterado para 200 OK para manter o padrão de mensagem no envelope
        return ResponseEntity.ok(new ApiResponse<>(null, "Despesa excluída com sucesso."));
    }
}