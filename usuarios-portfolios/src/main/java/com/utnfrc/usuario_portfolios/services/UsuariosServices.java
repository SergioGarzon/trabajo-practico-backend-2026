package com.utnfrc.usuario_portfolios.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.UsuariosRepositories;

@Service
public class UsuariosServices {

    @Autowired
    private UsuariosRepositories repo;

    public Usuarios create(Usuarios usuario) {
        return repo.save(usuario);
    }

    public List<Usuarios> getAll() {
        return repo.findAll();
    }

    public Optional<Usuarios> getById(String userID) {
        return repo.findById(userID);
    }

    public Usuarios update(String userID, Usuarios usuario) {
        // ensure the entity has the right id
        return repo.save(usuario);
    }

    public void delete(String userID) {
        repo.deleteById(userID);
    }
}
