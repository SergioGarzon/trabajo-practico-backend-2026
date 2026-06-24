package com.utnfrc.usuarios_portfolios_service.repositories;

import java.util.Optional;

import com.utnfrc.usuarios_portfolios_service.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuariosRepositories extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByKeycloakId(String keycloakId);

}
