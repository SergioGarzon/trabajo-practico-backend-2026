package com.utnfrc.usuario_portfolios.repositories;

import com.utnfrc.usuario_portfolios.models.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaSaldoRepository extends JpaRepository<OrdenCompra, Long> {
}
