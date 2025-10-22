package com.scaglia.financeiro.controller;

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
    public ResponseEntity<BalancoResponseDTO> obterBalancoMensal(
            @PathVariable int ano,
            @PathVariable int mes) {

        // Pequena validação para garantir que o mês está correto
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido. O mês deve ser entre 1 e 12.");
        }

        BalancoResponseDTO balanco = relatorioService.calcularBalancoMensal(mes, ano);
        return ResponseEntity.ok(balanco);
    }

    // Opcional: Endpoint que retorna o balanço do mês atual
    @Operation(summary = "Calcula o balanço total para o mês atual.")
    @GetMapping("/balanco/atual")
    public ResponseEntity<BalancoResponseDTO> obterBalancoAtual() {
        LocalDate hoje = LocalDate.now();
        BalancoResponseDTO balanco = relatorioService.calcularBalancoMensal(hoje.getMonthValue(), hoje.getYear());
        return ResponseEntity.ok(balanco);
    }
}
