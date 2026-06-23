package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.utnfrc.usuario_portfolios.models.Accion;

import java.util.Optional;

@Repository
public interface AccionRepository extends JpaRepository<Accion, Long> {
    Optional<Accion> findBySimboloIgnoreCase(String simbolo);
}
