package com.catedra.backend.compraventa.exception;

public class SinContraparteException extends RuntimeException {

    public SinContraparteException(String simboloAccion) {
        super("No se encontró contraparte compatible para el símbolo: " + simboloAccion);
    }
}
