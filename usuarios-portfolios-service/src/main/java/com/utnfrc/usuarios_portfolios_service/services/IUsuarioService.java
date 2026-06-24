package com.utnfrc.usuarios_portfolios_service.services;

import java.util.List;

import com.utnfrc.usuarios_portfolios_service.models.Usuario;

public interface IUsuarioService {

	Usuario registrarUsuario(Usuario usuario);
	
	Usuario obtenerPorDni(Long dni);
	
	Usuario obtenerPorKeycloakId(String keycloakId);
	
	List<Usuario> listarTodos();
}
