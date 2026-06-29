package utn.frc.bda.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.frc.bda.models.OrdenCompra;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    List<OrdenCompra> findByEstado(String estado);

}
