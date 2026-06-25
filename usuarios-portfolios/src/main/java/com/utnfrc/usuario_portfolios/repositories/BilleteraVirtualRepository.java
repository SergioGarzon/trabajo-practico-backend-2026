package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;

import java.util.Optional;

public interface BilleteraVirtualRepository extends JpaRepository<BilleteraVirtual, Long> {

    Optional<BilleteraVirtual> findByUsuario_Id(String usuarioID);
}