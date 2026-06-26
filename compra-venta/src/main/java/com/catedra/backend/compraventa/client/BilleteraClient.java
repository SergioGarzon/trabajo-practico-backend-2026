package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.SolicitudDineroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para el microservicio usuarios-portfolios.
 * Endpoint único: resuelve la operación de compra de forma atómica
 * (descuenta dinero y acredita las acciones al comprador).
 *
 * URL configurada vía propiedad: servicios.usuarios.url
 */
@FeignClient(name = "billetera-service", url = "${servicios.usuarios.url}")
public interface BilleteraClient {

    /**
     * Confirma o cancela una operación de billetera.
     * Para el flujo de compra exitosa usar estadoAccion = "CONFIRMAR".
     */
    @PutMapping("/api/billetera/operacion/resolver")
    void resolverOperacion(@RequestBody SolicitudDineroDTO requestDto);
}
