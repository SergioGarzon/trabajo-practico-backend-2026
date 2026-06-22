package com.catedra.backend.compraventa.repository;

import com.catedra.backend.compraventa.entity.OrdenCompra;
import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    List<OrdenCompra> findByUsuarioId(Long usuarioId);

    List<OrdenCompra> findBySimboloAccionAndEstado(String simboloAccion, EstadoOrden estado);
}
