package utn.frc.bda.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import utn.frc.bda.models.OrdenVenta;

@Repository
public interface OrdenVentaRepository extends JpaRepository<OrdenVenta, Long> {

    List<OrdenVenta> findBySimboloAccionAndEstadoInAndPrecioLessThanEqualOrderByPrecioAscFechaCreacionAsc(
            String simboloAccion, 
            List<String> estados, 
            Double precioMaximoComprador
    );
}