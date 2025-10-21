package com.scaglia.financeiro.enums;

public enum TipoMovimentacao {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
