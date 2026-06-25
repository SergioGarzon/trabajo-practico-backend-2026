package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.utnfrc.usuario_portfolios.models.Accion;

import java.util.Optional;

public interface AccionRepository extends JpaRepository<Accion, Long> {
    Optional<Accion> findBySimbolo(String simbolo);
}
