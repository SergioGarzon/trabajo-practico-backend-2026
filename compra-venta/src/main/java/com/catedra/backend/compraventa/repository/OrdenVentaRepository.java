package com.catedra.backend.compraventa.repository;

import com.catedra.backend.compraventa.entity.OrdenVenta;
import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenVentaRepository extends JpaRepository<OrdenVenta, Long> {

    List<OrdenVenta> findByUsuarioId(Long usuarioId);

    List<OrdenVenta> findBySimboloAccionAndEstado(String simboloAccion, EstadoOrden estado);

    List<OrdenVenta> findBySimboloAccionAndPrecioPorAccionLessThanEqualAndEstadoInAndCantidadRestanteGreaterThanOrderByPrecioPorAccionAsc(
            String simboloAccion, Double precioPorAccion, List<EstadoOrden> estados, Long cantidadRestante);
}

