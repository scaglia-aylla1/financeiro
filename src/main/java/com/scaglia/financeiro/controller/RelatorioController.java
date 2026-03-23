package com.scaglia.financeiro.controller;

import com.scaglia.financeiro.dto.ApiResponse; // Importando o envelope
import com.scaglia.financeiro.dto.BalancoResponseDTO;
import com.scaglia.financeiro.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Relatórios e Dashboard", description = "Endpoints para consulta do Balanço e Resumo Financeiro")
@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Operation(summary = "Calcula o balanço total (Receitas - Despesas) para um mês e ano específicos.")
    @GetMapping("/balanco/{ano}/{mes}")
    public ResponseEntity<ApiResponse<BalancoResponseDTO>> obterBalancoMensal(
            @PathVariable int ano,
            @PathVariable int mes) {

        // Nota: Validar mês/ano é excelente. 
        // Em um passo futuro, isso poderia ser tratado por um ExceptionHandler global.
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido. O mês deve ser entre 1 e 12.");
        }

        BalancoResponseDTO balanco = relatorioService.calcularBalancoMensal(mes, ano);
        
        return ResponseEntity.ok(new ApiResponse<>(balanco, "Balanço mensal calculado com sucesso."));
    }

    @Operation(summary = "Calcula o balanço total para o mês atual.")
    @GetMapping("/balanco/atual")
    public ResponseEntity<ApiResponse<BalancoResponseDTO>> obterBalancoAtual() {
        LocalDate hoje = LocalDate.now();
        BalancoResponseDTO balanco = relatorioService.calcularBalancoMensal(
                hoje.getMonthValue(), 
                hoje.getYear()
        );
        
        return ResponseEntity.ok(new ApiResponse<>(balanco, "Balanço do mês atual carregado."));
    }
}