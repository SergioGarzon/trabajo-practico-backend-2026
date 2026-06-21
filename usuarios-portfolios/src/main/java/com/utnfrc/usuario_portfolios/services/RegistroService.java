package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.UsuariosRepositories;
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
public class RegistroService {

    private final Keycloak keycloak;
    private final UsuariosRepositories usuarioRepository;
    private final BilleteraVirtualService billeteraService;

    private final String REALM_NAME = "users-tp"; // El nombre de tu Reino

    public RegistroService(Keycloak keycloak, UsuariosRepositories usuarioRepository, BilleteraVirtualService billeteraService) {
        this.keycloak = keycloak;
        this.usuarioRepository = usuarioRepository;
        this.billeteraService = billeteraService;
    }

    @Transactional // Asegura que si falla la escritura en MySQL, se aplique un rollback local
    public Usuarios registrarUsuarioCompleto(RegistroDTO dto) {

        // 1. DEFINIR EL USUARIO PARA KEYCLOAK
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(dto.getUsername());
        userRep.setEmail(dto.getEmail());
        userRep.setFirstName(dto.getNombre());
        userRep.setLastName(dto.getApellido());

        userRep.setEnabled(true);

        // Configurar la contraseña
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(dto.getPassword());
        cred.setTemporary(false); // Fijo, para que no pida cambiarla al iniciar
        userRep.setCredentials(List.of(cred));

        // 2. ENVIAR ORDEN DE CREACIÓN AL REINO
        Response response = keycloak.realm(REALM_NAME).users().create(userRep);

        if (response.getStatus() == 409) {
            throw new RuntimeException("El nombre de usuario o el correo electrónico ya están en uso.");
        } else if (response.getStatus() != 201) {
            throw new RuntimeException("Error al crear usuario en Keycloak. Código de estado: " + response.getStatus());
        }

        // 3. OBTENER EL UUID ASIGNADO POR KEYCLOAK
        // Keycloak retorna la URI del nuevo usuario en el header 'Location' (ej: /users/e3b24f11-...)
        String path = response.getLocation().getPath();
        String userIdUuid = path.substring(path.lastIndexOf("/") + 1);

        // 4. ASIGNAR EL ROL DENTRO DEL REINO
        // Buscamos la representación del rol que vino en el DTO (ej: "USER" o "ADMIN") dentro del Reino
        RoleRepresentation roleRep = keycloak.realm(REALM_NAME).roles().get(dto.getRol()).toRepresentation();

        // Apuntamos al recurso del usuario específico mediante su UUID y le asociamos el rol
        UserResource userResource = keycloak.realm(REALM_NAME).users().get(userIdUuid);
        userResource.roles().realmLevel().add(List.of(roleRep));

        // 5. GUARDAR LOCALMENTE EN TU MICROSERVICIO (MYSQL)
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setId(userIdUuid); // Reemplazamos el Long secuencial por el String UUID de Keycloak
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