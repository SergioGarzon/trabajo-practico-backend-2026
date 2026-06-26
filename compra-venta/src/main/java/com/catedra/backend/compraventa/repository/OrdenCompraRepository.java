package com.catedra.backend.compraventa.repository;

import com.catedra.backend.compraventa.models.OrdenCompra;
import com.catedra.backend.compraventa.models.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    List<OrdenCompra> findByUsuarioId(Long usuarioId);

    @Query("SELECT oc FROM OrdenCompra oc " +
           "WHERE oc.simboloAccion = :simbolo " +
           "AND oc.precioPorAccion >= :precioMinimo " +
           "AND oc.estado IN :estados " +
           "AND oc.cantidadRestante > 0 " +
           "ORDER BY oc.precioPorAccion DESC")
    List<OrdenCompra> findComprasCompatibles(@Param("simbolo") String simboloAccion,
                                              @Param("precioMinimo") Double precioPorAccion,
                                              @Param("estados") List<EstadoOrden> estados);
}
