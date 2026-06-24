package com.utnfrc.usuarios_portfolios_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utnfrc.usuarios_portfolios_service.models.Usuario;
import com.utnfrc.usuarios_portfolios_service.services.IUsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuariosControllers {
	
	@Autowired	
    private IUsuarioService usuariosService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuariosService.registrarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Usuario> obtenerPorDni(@PathVariable Long dni) {
        Usuario usuario = usuariosService.obtenerPorDni(dni);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<Usuario> obtenerPorKeycloakId(@PathVariable String keycloakId) {
        Usuario usuario = usuariosService.obtenerPorKeycloakId(keycloakId);
        return ResponseEntity.ok(usuario);
    }
    
    

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> lista = usuariosService.listarTodos();
        return ResponseEntity.ok((List<Usuario>) lista);
    }
}
