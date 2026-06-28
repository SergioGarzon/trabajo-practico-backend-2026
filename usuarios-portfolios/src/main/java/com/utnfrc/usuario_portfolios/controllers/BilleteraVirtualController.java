package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.services.BilleteraVirtualService;
import com.utnfrc.usuario_portfolios.services.IBilleteraVirtualService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billetera")
public class BilleteraVirtualController {

    private final IBilleteraVirtualService billeteraVirtualService;

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
        billeteraVirtualService.findAllBV();
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
            @RequestBody Long cantidadDinero)
    {
        String userId = jwt.getSubject();
        BilleteraVirtual bvAct = billeteraVirtualService.ingresarDinero(userId, cantidadDinero);
        return ResponseEntity.ok().body(bvAct);
    }


    @PutMapping("/operacion/bloquear")
    public ResponseEntity<Boolean> iniciarOperacion(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudDineroDTO dto) {

        try{
            String userId = jwt.getSubject();
            billeteraVirtualService.solicitarYBloquearDinero(userId, dto.getMonto(), dto.getIdOrdenCompra());
            return ResponseEntity.ok(true);

        }catch(Exception e){
            return ResponseEntity.ok(false);
        }
    }


    @PutMapping("/operacion/resolver")
    public ResponseEntity<SolicitudDineroDTO> resolverOperacion(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudDineroDTO dto) {

        String userId = jwt.getSubject();
        BilleteraVirtual bvActualizada = billeteraVirtualService.procesarRespuestaExterna(userId, dto);

        dto.setMensaje("Se realizo la compra exitosamente");

        return ResponseEntity.ok(dto);
    }



}
