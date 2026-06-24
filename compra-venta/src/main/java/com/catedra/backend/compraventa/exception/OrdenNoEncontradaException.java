package com.catedra.backend.compraventa.exception;

public class OrdenNoEncontradaException extends RuntimeException {

    public OrdenNoEncontradaException(Long id) {
        super("No se encontró ninguna orden con id: " + id);
    }
}
