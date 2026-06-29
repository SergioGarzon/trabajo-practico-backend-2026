package com.utnfrc.usuario_portfolios.services;


import java.util.List;
import java.util.Optional;

import com.utnfrc.usuario_portfolios.models.Usuarios;

public interface IUsuarioService {
	
    Usuarios create(Usuarios usuario);
    List<Usuarios> getAll();
    
    Optional<Usuarios> getById(String id); 
    
    Optional<Usuarios> getByDni(String dni); 

    Usuarios update(String userID, Usuarios usuario);
    void delete(String userID);
}


