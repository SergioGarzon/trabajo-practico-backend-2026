package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;

@Repository
public interface BilleteraVirtualRepository extends JpaRepository<BilleteraVirtual, Long> {

}