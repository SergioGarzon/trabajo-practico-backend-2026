package com.catedra.backend.compraventa.exception;

public class TransaccionNoEncontradaException extends RuntimeException {

    public TransaccionNoEncontradaException(Long id) {
        super("No se encontró ninguna transacción con id: " + id);
    }
}
