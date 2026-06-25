package com.utnfrc.usuario_portfolios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.utnfrc.usuario_portfolios.models.Usuarios;

public interface UsuariosRepositories extends JpaRepository<Usuarios, String> {

}
