package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.services.BilleteraVirtualService;
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

    private final BilleteraVirtualService billeteraVirtualService;

    public BilleteraVirtualController(BilleteraVirtualService billeteraVirtualService) {
        this.billeteraVirtualService = billeteraVirtualService;
    }

    // Se crea una biiletera asociada al usuario, se usa el id del JWT para mayor seguridad

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

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminarBilleteraVirtual(@PathVariable("id") Long id) {
//        if (!billeteraVirtualService.findBVById(id).isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        billeteraVirtualService.deleteBV(id);
//        return ResponseEntity.noContent().build();
//    }

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
    public ResponseEntity<Map<String, String>> iniciarOperacion(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudDineroDTO dto) {

        String userId = jwt.getSubject();
        String idTransaccion = billeteraVirtualService.solicitarYBloquearDinero(userId, dto.getMonto());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("idTransaccion", idTransaccion);
        respuesta.put("mensaje", "Dinero bloqueado. Guarde este ID para la resolución.");

        return ResponseEntity.ok(respuesta);
    }


    @PutMapping("/operacion/resolver")
    public ResponseEntity<BilleteraVirtual> resolverOperacion(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody SolicitudDineroDTO dto) {

        String userId = jwt.getSubject();
        BilleteraVirtual bvActualizada = billeteraVirtualService.procesarRespuestaExterna( dto                                               );

        return ResponseEntity.ok(bvActualizada);
    }

}
