package com.catedra.backend.compraventa.exception;

public class CotizacionNoEncontradaException extends RuntimeException {

    public CotizacionNoEncontradaException(String simbolo) {
        super("No se encontró cotización para el símbolo: " + simbolo);
    }
}
