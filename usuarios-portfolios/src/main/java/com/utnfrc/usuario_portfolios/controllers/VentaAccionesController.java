package com.utnfrc.usuario_portfolios.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utnfrc.usuario_portfolios.dtos.ResolucionVentaDTO;
import com.utnfrc.usuario_portfolios.dtos.SolicitudVentaDTO;
import com.utnfrc.usuario_portfolios.models.OrdenVenta;
import com.utnfrc.usuario_portfolios.services.IVentaAccionesService;
import com.utnfrc.usuario_portfolios.services.VentaAccionesService;

@RestController
@RequestMapping("/api/ventas")
public class VentaAccionesController {

    @Autowired
    private final IVentaAccionesService ventaAccionesService;

    public VentaAccionesController(VentaAccionesService ventaAccionesService) {
        this.ventaAccionesService = ventaAccionesService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<SolicitudVentaDTO> iniciarVenta(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudVentaDTO dto) {

        String userId = jwt.getSubject(); // Extraemos el usuario del Token seguro
        OrdenVenta or = ventaAccionesService.iniciarVenta(userId, dto);

        SolicitudVentaDTO respuesta =  new SolicitudVentaDTO();
        respuesta.setIdOrdenVenta(dto.getIdOrdenVenta());
        respuesta.setCantidadInicial(or.getCantidadInicial());
        respuesta.setCantidadRestante(or.getCantidadRestante());
        respuesta.setMensaje("Se bloquearon las acciones exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/procesar")
    public ResponseEntity<Boolean> procesarVenta(
            @RequestBody ResolucionVentaDTO dto) {

        // El usuarioId viaja en el DTO (patrón máquina-a-máquina, sin JWT)
        ventaAccionesService.procesarVenta(dto);

        // Retorna Boolean true para que RestTemplate pueda deserializarlo correctamente
        return ResponseEntity.ok(true);
    }

    @PutMapping("/cancelar/{idOrden}")
    public ResponseEntity<Map<String, String>> cancelarVenta(
            @PathVariable Long idOrden) {

        ventaAccionesService.cancelarOrdenVenta(idOrden);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Orden de venta cancelada. Las acciones volvieron a estar libres.");

        return ResponseEntity.ok(respuesta);
    }
}
