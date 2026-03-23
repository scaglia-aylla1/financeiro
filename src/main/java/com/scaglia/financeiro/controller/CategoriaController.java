package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.ApiResponse; // Importe o seu novo DTO
import com.scaglia.financeiro.dto.CategoriaRequestDTO;
import com.scaglia.financeiro.dto.CategoriaResponseDTO;
import com.scaglia.financeiro.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Endpoints para gerenciamento das Categorias de Movimentação")
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Lista todas as categorias (Receita/Despesa)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaResponseDTO>>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(new ApiResponse<>(categorias, "Categorias listadas com sucesso."));
    }

    @Operation(summary = "Busca uma categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> buscarCategoria(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(categoria, "Categoria encontrada."));
    }

    @Operation(summary = "Cria uma nova categoria")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> criarCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO novaCategoria = categoriaService.criarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(novaCategoria, "Categoria criada com sucesso."));
    }

    @Operation(summary = "Atualiza uma categoria existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDTO>> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO categoriaAtualizada = categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(categoriaAtualizada, "Categoria atualizada com sucesso."));
    }

    @Operation(summary = "Deleta uma categoria por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarCategoria(@PathVariable Long id) {
        categoriaService.deletarCategoria(id);
        // Em exclusão, você pode retornar 204 (NoContent) sem corpo, 
        // OU retornar 200 com uma mensagem confirmando a deleção.
        // Para seguir o padrão de "Envelope", vamos de 200 OK:
        return ResponseEntity.ok(new ApiResponse<>(null, "Categoria excluída com sucesso."));
    }
}