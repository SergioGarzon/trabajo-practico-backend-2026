package com.catedra.backend.compraventa.exception;

public class BloqueoFondosException extends RuntimeException {

    public BloqueoFondosException(Long usuarioId) {
        super("Error al bloquear fondos para el usuario: " + usuarioId);
    }
}
