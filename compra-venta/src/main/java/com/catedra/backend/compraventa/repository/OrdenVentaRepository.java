package com.catedra.backend.compraventa.repository;

import com.catedra.backend.compraventa.models.OrdenVenta;
import com.catedra.backend.compraventa.models.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenVentaRepository extends JpaRepository<OrdenVenta, Long> {

    List<OrdenVenta> findByUsuarioId(Long usuarioId);

    @Query("SELECT ov FROM OrdenVenta ov " +
           "WHERE ov.simboloAccion = :simbolo " +
           "AND ov.precioPorAccion <= :precioMaximo " +
           "AND ov.estado IN :estados " +
           "AND ov.cantidadRestante > 0 " +
           "ORDER BY ov.precioPorAccion ASC")
    List<OrdenVenta> findVentasCompatibles(@Param("simbolo") String simboloAccion,
                                           @Param("precioMaximo") Double precioPorAccion,
                                           @Param("estados") List<EstadoOrden> estados);
}
