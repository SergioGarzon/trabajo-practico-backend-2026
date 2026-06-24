package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.exception.CotizacionNoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.Map;

// IMPLEMENTACIÓN TEMPORAL (MOCK): Esta clase devuelve cotizaciones estáticas hardcodeadas.
//
// Cuando el microservicio de Cotizaciones esté listo, se debe:
// 1. Crear un FeignClient (ej. CotizacionClient) apuntando al microservicio de cotizaciones.
// 2. Crear una nueva implementación de CotizacionService (ej. CotizacionServiceImpl) que
//    inyecte ese FeignClient y delegue las consultas al microservicio real.
// 3. Marcar esa nueva implementación con @Service y @Primary para que Spring la use
//    en lugar de esta clase mock.
// 4. Opcionalmente, eliminar o anotar esta clase con @Profile("dev") para mantenerla
//    solo en entornos de desarrollo/testing.
@Service
public class CotizacionServiceMockImpl implements CotizacionService {

    private static final Map<String, Double> COTIZACIONES = Map.of(
            "NVDA", 35000.0,
            "AAPL", 28000.0,
            "GOOGL", 22000.0,
            "TSLA", 31000.0,
            "AMZN", 26000.0
    );

    @Override
    public Double obtenerPrecioPorSimbolo(String simbolo) {
        Double precio = COTIZACIONES.get(simbolo.toUpperCase());
        if (precio == null) {
            throw new CotizacionNoEncontradaException(simbolo);
        }
        return precio;
    }

    @Override
    public boolean existeSimbolo(String simbolo) {
        return COTIZACIONES.containsKey(simbolo.toUpperCase());
    }
}
