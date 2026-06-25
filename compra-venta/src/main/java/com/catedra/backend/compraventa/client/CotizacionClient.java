package com.catedra.backend.compraventa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cotizaciones-service", url = "${servicios.cotizaciones.url}")
public interface CotizacionClient {

    @GetMapping("/servidormock/api/v1/stocks/quota/{symbol}")
    String obtenerCotizacionCruda(@PathVariable("symbol") String symbol);
}
