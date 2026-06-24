package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.PortfolioOperacionRequestDto;
import com.catedra.backend.compraventa.dto.PortfolioTenenciaResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// VERIFICAR: confirmar con el equipo de Usuarios que las rutas
// GET /api/portfolio/{usuarioId}/tenencia/{simbolo} y PUT /api/portfolio/actualizar
// coincidan exactamente con las de su controlador de Portfolio.
@FeignClient(name = "portfolio-service", url = "${servicios.usuarios.url}")
public interface PortfolioClient {

    // Consulta la cantidad de acciones de un símbolo que posee el usuario.
    @GetMapping("/api/portfolio/{usuarioId}/tenencia/{simbolo}")
    PortfolioTenenciaResponseDto consultarTenencia(@PathVariable("usuarioId") Long usuarioId,
                                                   @PathVariable("simbolo") String simbolo);

    // Suma o resta tenencia del usuario tras una transacción ejecutada.
    @PutMapping("/api/portfolio/actualizar")
    void actualizarTenencia(@RequestBody PortfolioOperacionRequestDto requestDto);
}
