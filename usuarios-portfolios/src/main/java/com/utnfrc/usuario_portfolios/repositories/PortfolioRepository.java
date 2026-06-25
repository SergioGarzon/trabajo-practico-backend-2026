package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.utnfrc.usuario_portfolios.models.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

}