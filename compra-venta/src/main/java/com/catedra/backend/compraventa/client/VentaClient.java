package com.catedra.backend.compraventa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.catedra.backend.compraventa.dto.ResolucionVentaDTO;

@FeignClient(name = "venta-service", url = "${servicios.usuarios.url}")
public interface VentaClient {

    @PutMapping("/api/ventas/procesar")
    void procesarVenta(@RequestBody ResolucionVentaDTO dto);
}
