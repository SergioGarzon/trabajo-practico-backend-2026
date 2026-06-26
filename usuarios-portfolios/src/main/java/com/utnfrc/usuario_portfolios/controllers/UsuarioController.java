package com.utnfrc.usuario_portfolios.controllers;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.UsuarioService;
import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.services.RegistroService;
import com.utnfrc.usuario_portfolios.exception.ResourceNotFoundException;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final RegistroService registroService;

    public UsuarioController(UsuarioService service, RegistroService registroService) {
        this.service = service;
        this.registroService = registroService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuarios> create(@RequestBody RegistroDTO dto) {
        Usuarios created = registroService.registrarUsuarioCompleto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Usuarios>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Usuarios> getById(@AuthenticationPrincipal Jwt jwt) {
        String userID = jwt.getSubject();
        return service.getById(userID)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @PutMapping("/update")
    public ResponseEntity<Usuarios> update(@AuthenticationPrincipal Jwt jwt, @RequestBody Usuarios usuario) {
        String userID = jwt.getSubject();
        validarUsuarioExiste(userID);
        Usuarios updated = service.update(userID, usuario);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt) {
        String userID = jwt.getSubject();
        validarUsuarioExiste(userID);

        service.delete(userID);
        return ResponseEntity.noContent().build();
    }

    private void validarUsuarioExiste(String userId) {
        if (!service.getById(userId).isPresent()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
    }
}
