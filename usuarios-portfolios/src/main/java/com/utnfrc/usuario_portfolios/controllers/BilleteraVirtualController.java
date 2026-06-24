package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.services.BilleteraVirtualService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billetera")
public class BilleteraVirtualController {

    private final BilleteraVirtualService billeteraVirtualService;

    public BilleteraVirtualController(BilleteraVirtualService billeteraVirtualService) {
        this.billeteraVirtualService = billeteraVirtualService;
    }

    @PostMapping("/crear")
    public ResponseEntity<BilleteraVirtual> crearBilleteraVirtual(
            @AuthenticationPrincipal Jwt jwt) {
        String userID = jwt.getSubject();
        BilleteraVirtual bvNew = billeteraVirtualService.createBV(userID);
        return ResponseEntity.ok().body(bvNew);
    }

    @GetMapping()
    public ResponseEntity<List<BilleteraVirtual>> obtenerBilleteraVirtuales() {
        return ResponseEntity.ok().body(billeteraVirtualService.findAllBV());
    }

    @PutMapping("/retirar")
    public ResponseEntity<BilleteraVirtual> retirarDinero(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Long cantidadDinero) {
        String usuarioId = jwt.getSubject();
        BilleteraVirtual newBV = billeteraVirtualService.retirarDinero(usuarioId, cantidadDinero);
        return ResponseEntity.ok().body(newBV);
    }

    @PutMapping("/ingresar")
    public ResponseEntity<BilleteraVirtual> ingresarDinero(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Long cantidadDinero) {
        String userId = jwt.getSubject();
        BilleteraVirtual bvAct = billeteraVirtualService.ingresarDinero(userId, cantidadDinero);
        return ResponseEntity.ok().body(bvAct);
    }
}

