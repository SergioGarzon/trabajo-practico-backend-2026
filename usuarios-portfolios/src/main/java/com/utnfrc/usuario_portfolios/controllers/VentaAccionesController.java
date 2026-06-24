package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.dtos.ResolucionVentaDTO;
import com.utnfrc.usuario_portfolios.dtos.SolicitudVentaDTO;
import com.utnfrc.usuario_portfolios.repositories.OrdenVentaRepository;
import com.utnfrc.usuario_portfolios.services.VentaAccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaAccionesController {

    @Autowired
    private final VentaAccionesService ventaAccionesService;

    public VentaAccionesController(VentaAccionesService ventaAccionesService) {
        this.ventaAccionesService = ventaAccionesService;
    }
    /**
     * FASE 1: El usuario pide iniciar una venta (Se bloquean sus acciones)
     */
    @PostMapping("/iniciar")
    public ResponseEntity<Map<String, String>> iniciarVenta(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudVentaDTO dto) {

        String userId = jwt.getSubject(); // Extraemos el usuario del Token seguro
        String idOrden = ventaAccionesService.iniciarVenta(userId, dto);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("idOrdenVenta", idOrden);
        respuesta.put("mensaje", "Orden de venta creada. Las acciones han sido bloqueadas.");

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * FASE 2: El motor externo de emparejamiento avisa que se concretó una venta (parcial o total)
     */
    @PutMapping("/procesar")
    public ResponseEntity<Map<String, String>> procesarVenta(
            @RequestBody ResolucionVentaDTO dto) {

        ventaAccionesService.procesarVenta(dto);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Venta procesada exitosamente. Dinero acreditado en la billetera.");

        return ResponseEntity.ok(respuesta);
    }

    /**
     * EXTRA: El usuario se arrepiente y cancela la orden de venta (Se liberan las acciones retenidas)
     */
    @PutMapping("/cancelar/{idOrden}")
    public ResponseEntity<Map<String, String>> cancelarVenta(
            @PathVariable String idOrden) {

        ventaAccionesService.cancelarOrdenVenta(idOrden);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Orden de venta cancelada. Las acciones volvieron a estar libres.");

        return ResponseEntity.ok(respuesta);
    }
}
