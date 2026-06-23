package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.PortfolioOperacionRequestDto;
import com.catedra.backend.compraventa.dto.PortfolioTenenciaResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "portfolio-service", url = "${servicios.usuarios.url}")
public interface PortfolioClient {

    @GetMapping("/api/portfolio/tenencia")
    PortfolioTenenciaResponseDto verificarTenencia(@RequestParam Long usuarioId,
                                                   @RequestParam String simboloAccion);

    @PutMapping("/api/portfolio/descontar")
    void descontarAcciones(@RequestBody PortfolioOperacionRequestDto requestDto);

    @PutMapping("/api/portfolio/agregar")
    void agregarAcciones(@RequestBody PortfolioOperacionRequestDto requestDto);
}
