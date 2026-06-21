package com.utnfrc.cotizaciones.clients;

import com.utnfrc.cotizaciones.dtos.CotizacionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CotizacionApiClient {

    private final RestTemplate restTemplate;

    public CotizacionApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CotizacionDTO obtenerCotizacion(String simbolo) {
        return new CotizacionDTO();
    }

}
