package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.utnfrc.usuario_portfolios.models.Usuarios;

@Repository
public interface UsuariosRepositories extends JpaRepository<Usuarios, Long> {

}
