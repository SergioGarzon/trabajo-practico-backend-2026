package utn.frc.bda.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utn.frc.bda.dtos.TransaccionDTO;
import utn.frc.bda.services.TransaccionService;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    /**
     * GET /api/transacciones
     * Devuelve el historial completo de transacciones ejecutadas por el motor
     * de emparejamiento.
     *
     * @return 200 OK con la lista de TransaccionDTO, o 204 No Content si está vacía.
     */
    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> obtenerTodasLasTransacciones() {
        List<TransaccionDTO> transacciones = transaccionService.obtenerTodasLasTransacciones();

        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transacciones);
    }
}
