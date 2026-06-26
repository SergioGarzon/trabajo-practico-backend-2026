package com.utnfrc.usuario_portfolios.services;



import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.UsuarioRepository;
import com.utnfrc.usuario_portfolios.excepciones.ResourceNotFoundException;


@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

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
