package com.utnfrc.usuario_portfolios.repositories;

import com.utnfrc.usuario_portfolios.models.RegistroOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroOperacionRepository extends JpaRepository<RegistroOperacion, Long> {

    List<RegistroOperacion> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);
}