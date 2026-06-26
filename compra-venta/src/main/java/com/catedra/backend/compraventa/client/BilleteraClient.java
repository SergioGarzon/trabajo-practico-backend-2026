package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.SolicitudDineroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// VERIFICAR: confirmar con el equipo de Usuarios que las rutas /api/billetera/operacion/bloquear
// y /api/billetera/operacion/resolver coincidan exactamente con las de su controlador.
@FeignClient(name = "billetera-service", url = "${servicios.usuarios.url}")
public interface BilleteraClient {

    // Congela fondos preventivamente antes de intentar el emparejamiento.
    @PutMapping("/api/billetera/operacion/bloquear")
    Object bloquearFondos(@RequestBody SolicitudDineroDTO requestDto);

    // Confirma (éxito) o libera (cancelación) los fondos previamente congelados.
    @PutMapping("/api/billetera/operacion/resolver")
    Object resolverOperacion(@RequestBody SolicitudDineroDTO requestDto);
}
