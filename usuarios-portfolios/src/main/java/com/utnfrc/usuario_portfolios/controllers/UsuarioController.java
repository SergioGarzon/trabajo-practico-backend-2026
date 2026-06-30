package com.utnfrc.usuario_portfolios.controllers;



import com.utnfrc.usuario_portfolios.dtos.UsuarioDTO;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.UsuarioService;
import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.services.IRegistroService;
import com.utnfrc.usuario_portfolios.services.IUsuarioService;
import com.utnfrc.usuario_portfolios.services.RegistroService;
import com.utnfrc.usuario_portfolios.excepciones.ResourceNotFoundException;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService service;
    private final IRegistroService registroService;

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
    public ResponseEntity<List<UsuarioDTO>> all() {
        List<UsuarioDTO> lUser = service.getAll().stream()
                .map(usuario -> {
                    BilleteraVirtual bv = usuario.getBilleteraVirtual();
                    Portfolio portfolio = usuario.getPortfolio();

                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setNombre(usuario.getNombre());
                    dto.setApellido(usuario.getApellido());
                    dto.setUsername(usuario.getNombre()+usuario.getApellido());
                    dto.setDineroLibre(bv.getDineroLibre());
                    dto.setDineroInvertido(bv.getDineroInvertido());
                    dto.setDineroBloqueado(bv.getDineroBloqueado());
                    dto.setDineroTotal(bv.getDineroLibre() +  bv.getDineroInvertido() + bv.getDineroBloqueado());
                    dto.setPortfolio(portfolio);
                    return dto;
                }).toList();
        return ResponseEntity.ok(lUser);
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

    @DeleteMapping("/eliminar")
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
