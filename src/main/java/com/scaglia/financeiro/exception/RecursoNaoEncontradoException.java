package com.scaglia.financeiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Resposta HTTP 404
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String nomeRecurso, Long id) {
        super(String.format("%s com ID %d não encontrado.", nomeRecurso, id));
    }
}
