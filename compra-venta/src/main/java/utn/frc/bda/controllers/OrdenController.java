package utn.frc.bda.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utn.frc.bda.dtos.NuevaOrdenCompraDTO;
import utn.frc.bda.dtos.NuevaOrdenVentaDTO;
import utn.frc.bda.interfaces.OrdenCompraInterface;
import utn.frc.bda.interfaces.OrdenVentaInterfaz;
import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.services.EmparejamientoService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenCompraInterface ordenCompraService;
    private final OrdenVentaInterfaz ordenVentaService;
    private final EmparejamientoService emparejamientoService;

    public OrdenController(OrdenCompraInterface ordenCompraService, OrdenVentaInterfaz ordenVentaService, EmparejamientoService emparejamientoService) {
        this.ordenCompraService = ordenCompraService;
        this.ordenVentaService = ordenVentaService;
        this.emparejamientoService = emparejamientoService;
    }

    @PostMapping("/compra")
    public ResponseEntity<?> crearOrdenCompra(@RequestBody NuevaOrdenCompraDTO dto, @AuthenticationPrincipal Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            String jwtToken = jwt.getTokenValue();

            OrdenCompra orden = ordenCompraService.registrarOrdenCompra(
                    userId, 
                    dto.getSimbolo(), 
                    dto.getCantidad(), 
                    dto.getMontoMaximo(), 
                    jwtToken
            );

            // Se guarda, y va al motor de emparejamiento
            emparejamientoService.procesarNuevaCompra(orden);

            return ResponseEntity.ok("Orden de compra creada y procesada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/venta")
    public ResponseEntity<?> crearOrdenVenta(@RequestBody NuevaOrdenVentaDTO dto, @AuthenticationPrincipal Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            String jwtToken = jwt.getTokenValue();

            OrdenVenta orden = ordenVentaService.registrarOrdenVenta(
                    userId, 
                    dto.getSimbolo(), 
                    dto.getCantidad(), 
                    dto.getMontoMinimo(), 
                    jwtToken
            );

            // Se puede hacer un emparejamientoService.procesarNuevaVenta(orden) aca también (Hablar con Gaston)
            // por si quisieramos emparejar cuando entra una venta nueva, por ahora devuleve OK(se supone).
            
            return ResponseEntity.ok("Orden de venta creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
