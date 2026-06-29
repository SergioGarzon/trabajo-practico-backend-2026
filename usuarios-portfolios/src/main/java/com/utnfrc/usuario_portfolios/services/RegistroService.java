package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.UsuarioRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Service
public class RegistroService implements IRegistroService {

    private final Keycloak keycloak;
    private final UsuarioRepository usuarioRepository;
    private final BilleteraVirtualService billeteraService;

    private final String REALM_NAME = "users-tp";

    public RegistroService(Keycloak keycloak, UsuarioRepository usuarioRepository, BilleteraVirtualService billeteraService) {
        this.keycloak = keycloak;
        this.usuarioRepository = usuarioRepository;
        this.billeteraService = billeteraService;
    }

    @Transactional
    public Usuarios registrarUsuarioCompleto(RegistroDTO dto) {

        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(dto.getUsername());
        userRep.setEmail(dto.getEmail());
        userRep.setFirstName(dto.getNombre());
        userRep.setLastName(dto.getApellido());

        userRep.setEnabled(true);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(dto.getPassword());
        cred.setTemporary(false);
        userRep.setCredentials(List.of(cred));

        Response response = keycloak.realm(REALM_NAME).users().create(userRep);

        if (response.getStatus() == 409) {
            throw new RuntimeException("El nombre de usuario o el correo electrónico ya están en uso.");
        } else if (response.getStatus() != 201) {
            throw new RuntimeException("Error al crear usuario en Keycloak. Código de estado: " + response.getStatus());
        }

        String path = response.getLocation().getPath();
        String userIdUuid = path.substring(path.lastIndexOf("/") + 1);

        RoleRepresentation roleRep = keycloak.realm(REALM_NAME).roles().get(dto.getRol()).toRepresentation();

        UserResource userResource = keycloak.realm(REALM_NAME).users().get(userIdUuid);
        userResource.roles().realmLevel().add(List.of(roleRep));

        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setId(userIdUuid);
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setDni(dto.getDni());
        nuevoUsuario.setApellido(dto.getApellido());
        nuevoUsuario.setDomicilio(dto.getDomicilio());
        nuevoUsuario.setRol(dto.getRol());
        Portfolio portfolio = new Portfolio();
        nuevoUsuario.setPortfolio(portfolio);

        Usuarios usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return usuarioGuardado;
    }
}