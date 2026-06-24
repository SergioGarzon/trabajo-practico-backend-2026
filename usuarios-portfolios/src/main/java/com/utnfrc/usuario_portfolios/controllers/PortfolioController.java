package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.dtos.PortfolioOperacionRequestDto;
import com.utnfrc.usuario_portfolios.dtos.PortfolioTenenciaResponseDto;
import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.models.ItemPortfolio;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.AccionService;
import com.utnfrc.usuario_portfolios.services.PortfolioService;
import com.utnfrc.usuario_portfolios.services.UsuariosServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final UsuariosServices usuariosServices;
    private final PortfolioService portfolioService;
    private final AccionService accionService;

    public PortfolioController(UsuariosServices usuariosServices,
                               PortfolioService portfolioService,
                               AccionService accionService) {
        this.usuariosServices = usuariosServices;
        this.portfolioService = portfolioService;
        this.accionService = accionService;
    }

    @GetMapping("/{usuarioId}/tenencia/{simbolo}")
    public ResponseEntity<PortfolioTenenciaResponseDto> consultarTenencia(
            @PathVariable Long usuarioId,
            @PathVariable String simbolo) {

        Usuarios usuario = usuariosServices.getByNumericId(usuarioId);
        Portfolio portfolio = usuario.getPortfolio();

        PortfolioTenenciaResponseDto response = new PortfolioTenenciaResponseDto();
        response.setUsuarioId(usuarioId);
        response.setSimboloAccion(simbolo.toUpperCase());
        response.setCantidadDisponible(0L);

        if (portfolio != null) {
            ItemPortfolio item = portfolioService.findItemBySimbolo(portfolio, simbolo);
            if (item != null) {
                response.setCantidadDisponible(item.getCantidadLibre());
            }
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Void> actualizarTenencia(
            @Valid @RequestBody PortfolioOperacionRequestDto requestDto) {

        Usuarios usuario = usuariosServices.getByNumericId(requestDto.getUsuarioId());
        Portfolio portfolio = usuario.getPortfolio();

        if (portfolio == null) {
            throw new IllegalStateException("El usuario no tiene un portfolio asociado.");
        }

        String simbolo = requestDto.getSimboloAccion().toUpperCase();
        Long cantidad = requestDto.getCantidad();

        if (cantidad > 0) {
            Accion accion = accionService.buscarPorSimbolo(simbolo);
            if (accion == null) {
                throw new IllegalArgumentException("La accion con simbolo " + simbolo + " no existe.");
            }
            portfolioService.agregarAccion(portfolio.getId().toString(), accion, cantidad);
        } else if (cantidad < 0) {
            portfolioService.restarAccion(portfolio.getId().toString(), simbolo, Math.abs(cantidad));
        }

        return ResponseEntity.ok().build();
    }
}
