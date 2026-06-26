package com.catedra.backend.compraventa.repository;

import com.catedra.backend.compraventa.models.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByOrdenCompraId(Long ordenCompraId);

    List<Transaccion> findByOrdenVentaId(Long ordenVentaId);

    @Query("SELECT t FROM Transaccion t WHERE t.ordenCompra.usuarioId = :usuarioId OR t.ordenVenta.usuarioId = :usuarioId ORDER BY t.fecha DESC")
    List<Transaccion> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT t FROM Transaccion t ORDER BY t.fecha DESC")
    List<Transaccion> findAllOrderByFechaDesc();
}
