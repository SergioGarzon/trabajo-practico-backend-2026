package com.catedra.backend.compraventa.exception;

public class TenenciaInsuficienteException extends RuntimeException {

    public TenenciaInsuficienteException(Long usuarioId, String simboloAccion) {
        super("Tenencia insuficiente del símbolo " + simboloAccion + " para el usuario: " + usuarioId);
    }
}
