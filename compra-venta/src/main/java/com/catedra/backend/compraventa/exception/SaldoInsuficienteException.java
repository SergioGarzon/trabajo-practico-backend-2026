package com.catedra.backend.compraventa.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(Long usuarioId, Double montoRequerido) {
        super("Saldo insuficiente para el usuario " + usuarioId + ". Monto requerido: " + montoRequerido);
    }
}
