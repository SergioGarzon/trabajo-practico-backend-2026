package com.utnfrc.usuarios_portfolios_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utnfrc.usuarios_portfolios_service.models.Portfolio;

@Repository
public interface IPortfolioRepository extends JpaRepository<Portfolio, Long>{

}
