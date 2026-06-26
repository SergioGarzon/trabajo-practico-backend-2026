package com.utnfrc.usuario_portfolios.exception;


public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }

    public SaldoInsuficienteException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
