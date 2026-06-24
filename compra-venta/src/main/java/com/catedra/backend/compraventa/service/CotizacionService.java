package com.catedra.backend.compraventa.service;

public interface CotizacionService {

    Double obtenerPrecioPorSimbolo(String simbolo);

    boolean existeSimbolo(String simbolo);
}
