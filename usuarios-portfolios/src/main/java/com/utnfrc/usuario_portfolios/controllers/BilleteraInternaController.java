package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.dtos.BilleteraOperacionRequestDto;
import com.utnfrc.usuario_portfolios.dtos.BilleteraOperacionResponseDto;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.BilleteraVirtualService;
import com.utnfrc.usuario_portfolios.services.UsuariosServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billetera/operacion")
public class BilleteraInternaController {

    private final BilleteraVirtualService billeteraVirtualService;
    private final UsuariosServices usuariosServices;

    public BilleteraInternaController(BilleteraVirtualService billeteraVirtualService,
                                      UsuariosServices usuariosServices) {
        this.billeteraVirtualService = billeteraVirtualService;
        this.usuariosServices = usuariosServices;
    }

    @PutMapping("/bloquear")
    public ResponseEntity<BilleteraOperacionResponseDto> bloquearFondos(
            @Valid @RequestBody BilleteraOperacionRequestDto requestDto) {

        Usuarios usuario = usuariosServices.getByNumericId(requestDto.getUsuarioId());
        String keycloakId = usuario.getId();

        try {
            String idTransaccion = billeteraVirtualService.solicitarYBloquearDinero(
                    keycloakId, requestDto.getMonto().longValue());

            BilleteraOperacionResponseDto response = new BilleteraOperacionResponseDto();
            response.setExitoso(true);
            response.setMensaje(idTransaccion);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            BilleteraOperacionResponseDto response = new BilleteraOperacionResponseDto();
            response.setExitoso(false);
            response.setMensaje(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/resolver")
    public ResponseEntity<BilleteraOperacionResponseDto> resolverOperacion(
            @Valid @RequestBody BilleteraOperacionRequestDto requestDto) {

        Usuarios usuario = usuariosServices.getByNumericId(requestDto.getUsuarioId());
        String keycloakId = usuario.getId();

        try {
            BilleteraVirtual bv = billeteraVirtualService.ingresarDinero(
                    keycloakId, requestDto.getMonto().longValue());

            BilleteraOperacionResponseDto response = new BilleteraOperacionResponseDto();
            response.setExitoso(true);
            response.setMensaje("Fondos resueltos correctamente.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            BilleteraOperacionResponseDto response = new BilleteraOperacionResponseDto();
            response.setExitoso(false);
            response.setMensaje(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
