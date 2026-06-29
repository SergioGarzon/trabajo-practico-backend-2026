package utn.frc.bda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import utn.frc.bda.models.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    // Más adelante, si necesitamos buscar el historial de transacciones de un usuario, 
    // agregaremos métodos de búsqueda personalizados aquí. Por ahora, con JpaRepository alcanza.
}