package com.catedra.backend.compraventa.exception;

public class MicroservicioFallaException extends RuntimeException {

    public MicroservicioFallaException(String nombreServicio, String detalle) {
        super("Fallo en la comunicación con el microservicio '" + nombreServicio + "': " + detalle);
    }

    public MicroservicioFallaException(String nombreServicio, Throwable causa) {
        super("Fallo en la comunicación con el microservicio '" + nombreServicio + "': " + causa.getMessage(), causa);
    }
}
