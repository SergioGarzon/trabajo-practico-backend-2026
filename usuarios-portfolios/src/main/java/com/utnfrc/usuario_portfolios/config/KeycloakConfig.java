package com.utnfrc.usuario_portfolios.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180") // URL de tu contenedor Keycloak
                .realm("master") // El cliente de administración se autentica en el realm master
                .username("admin") // Tu usuario administrador de Keycloak
                .password("admin") // Tu contraseña de administrador de Keycloak
                .clientId("admin-cli") // Cliente nativo de Keycloak para estas operaciones
                .build();
    }
}
