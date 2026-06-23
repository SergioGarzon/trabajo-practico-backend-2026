package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.exception.CotizacionNoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CotizacionService {

    private static final Map<String, Double> COTIZACIONES = Map.of(
            "NVDA", 35000.0,
            "AAPL", 28000.0,
            "GOOGL", 22000.0,
            "TSLA", 31000.0,
            "AMZN", 26000.0
    );

    public Double obtenerPrecioPorSimbolo(String simbolo) {
        Double precio = COTIZACIONES.get(simbolo.toUpperCase());
        if (precio == null) {
            throw new CotizacionNoEncontradaException(simbolo);
        }
        return precio;
    }

    public boolean existeSimbolo(String simbolo) {
        return COTIZACIONES.containsKey(simbolo.toUpperCase());
    }
}
