package com.utnfrc.usuario_portfolios.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.UsuariosServices;
import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.services.RegistroService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosControllers {

    @Autowired
    private UsuariosServices service;

    @Autowired
    private RegistroService registroService;

    //Este post crea a un nuevo Usuario tanto en la base de datos como en el Keycloak
    //{
    //    "username" : "GastonB",
    //    "email" : "Gaston@gmail.com",
    //    "password" : "123456",
    //    "nombre" : "Gaston",
    //    "rol" : "Admin",
    //    "dni" : 3566544,
    //    "apellido": "Blangino",
    //    "domicilio" : "Moron 2655"
    //}

    @PostMapping("/registro")
    public ResponseEntity<Usuarios> create(@RequestBody RegistroDTO dto) {
        System.out.println("Intentando registrar usuario: " + dto.getUsername());

        Usuarios created = registroService.registrarUsuarioCompleto(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }



    @GetMapping
    public ResponseEntity<List<Usuarios>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    //Como el id usado es el mismo que hace el Keycloak siempre lo obtenemos del JWT

    @GetMapping("/{dni}")
    public ResponseEntity<Usuarios> getById(@AuthenticationPrincipal Jwt jwt) {
        String userID = jwt.getSubject();

        return service.getById(userID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Usuarios> update(@AuthenticationPrincipal Jwt jwt, @RequestBody Usuarios usuario) {
        String userID = jwt.getSubject();

        if (!service.getById(userID).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Usuarios updated = service.update(userID, usuario);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt) {
        String userID = jwt.getSubject();
        if (!service.getById(userID).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(userID);
        return ResponseEntity.noContent().build();
    }
}