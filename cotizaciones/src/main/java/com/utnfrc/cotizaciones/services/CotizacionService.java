package com.utnfrc.cotizaciones.services;

import com.utnfrc.cotizaciones.clients.CotizacionApiClient;
import com.utnfrc.cotizaciones.dtos.CotizacionDTO;
import org.springframework.stereotype.Service;

@Service
public class CotizacionService {

    private final CotizacionApiClient cotizacionApiClient;

    public CotizacionService(CotizacionApiClient cotizacionApiClient) {
        this.cotizacionApiClient = cotizacionApiClient;
    }

    public CotizacionDTO obtenerCotizacionEnARS(String simbolo) {
        CotizacionDTO cotizacion = cotizacionApiClient.obtenerCotizacion(simbolo);
        return cotizacion;
    }

}
