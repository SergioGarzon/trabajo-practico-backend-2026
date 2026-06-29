package utn.frc.bda.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utn.frc.bda.dtos.TransaccionDTO;
import utn.frc.bda.models.Transaccion;
import utn.frc.bda.repositories.TransaccionRepository;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * Devuelve el historial completo de transacciones ejecutadas por el motor
     * de emparejamiento, mapeando cada entidad Transaccion a un TransaccionDTO.
     */
    @Transactional(readOnly = true)
    public List<TransaccionDTO> obtenerTodasLasTransacciones() {
        List<Transaccion> transacciones = transaccionRepository.findAll();

        return transacciones.stream()
                .map(t -> new TransaccionDTO(
                        t.getId(),
                        t.getOrdenCompra().getId(),   // Extracción manual del ID de la orden de compra
                        t.getOrdenVenta().getId(),    // Extracción manual del ID de la orden de venta
                        t.getSimboloAccion(),
                        t.getCantidad(),
                        t.getPrecioAcordado(),
                        t.getFechaTransaccion()
                ))
                .collect(Collectors.toList());
    }
}
