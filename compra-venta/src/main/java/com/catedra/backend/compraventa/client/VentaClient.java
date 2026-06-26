package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.ResolucionVentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para el microservicio usuarios-portfolios.
 * Resuelve la operación de venta de forma atómica:
 * descuenta las acciones del portfolio y acredita el dinero al vendedor.
 *
 * URL configurada vía propiedad: servicios.usuarios.url
 */
@FeignClient(name = "venta-service", url = "${servicios.usuarios.url}")
public interface VentaClient {

    @PutMapping("/api/ventas/procesar")
    void procesarVenta(@RequestBody ResolucionVentaDTO dto);
}
