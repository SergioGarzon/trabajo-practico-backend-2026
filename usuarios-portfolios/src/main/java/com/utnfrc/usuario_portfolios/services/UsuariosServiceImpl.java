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

    public Optional<Usuarios> getById(Long dni) {
        return repo.findById(dni);
    }

    public Usuarios update(Long dni, Usuarios usuario) {
        // ensure the entity has the right id
        usuario.setDni(dni);
        return repo.save(usuario);
    }

    public void delete(Long dni) {
        repo.deleteById(dni);
    }
}
