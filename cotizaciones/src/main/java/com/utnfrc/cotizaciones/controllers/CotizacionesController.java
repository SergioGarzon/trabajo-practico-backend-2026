package com.utnfrc.cotizaciones.controllers;

import com.utnfrc.cotizaciones.dtos.CotizacionDTO;
import com.utnfrc.cotizaciones.services.CotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionesController {

    private final CotizacionService cotizacionService;

    public CotizacionesController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @GetMapping("/{simbolo}")
    public ResponseEntity<CotizacionDTO> getCotizacion(@PathVariable String simbolo) {
        return ResponseEntity.ok(cotizacionService.obtenerCotizacionEnARS(simbolo));
    }

}
