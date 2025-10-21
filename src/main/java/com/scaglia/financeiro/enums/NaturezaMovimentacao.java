package com.scaglia.financeiro.enums;

public enum NaturezaMovimentacao {
    FIXA("Fixo"),
    VARIAVEL("Variável");

    private final String descricao;

    NaturezaMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
