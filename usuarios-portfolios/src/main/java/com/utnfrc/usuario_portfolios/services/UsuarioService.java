package com.utnfrc.usuario_portfolios.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.UsuarioRepository;
import com.utnfrc.usuario_portfolios.excepciones.ResourceNotFoundException;


@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public Usuarios create(Usuarios usuario) {
        return repo.save(usuario);
    }

    public List<Usuarios> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Usuarios> getById(String id) {
        return repo.findById(id); // Busca por la clave primaria real (ID Keycloak)
    }

    @Override
    public Optional<Usuarios> getByDni(String dni) {
        return repo.findByDni(dni); // <-- Cambiar findById por findByDni
    }

    public Usuarios update(String userID, Usuarios usuario) {
        if (!repo.existsById(userID)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + userID);
        }
        usuario.setId(userID);
        return repo.save(usuario);
    }

    public void delete(String userID) {
        if (!repo.existsById(userID)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + userID);
        }
        repo.deleteById(userID);
    }
}
