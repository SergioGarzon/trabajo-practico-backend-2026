package utn.frc.edu.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	// Lee la URL de Keycloak directamente desde tus propiedades locales
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // REQUERIMIENTO OBLIGATORIO: La consulta de acciones es de ACCESO PÚBLICO
                        .pathMatchers("/servidormock/api/v1/stocks/**").permitAll()
                        .pathMatchers("/api/usuarios/registro").permitAll()
                        .pathMatchers("/api/ventas/iniciar").permitAll()
                        // Cualquier otra ruta protegida exige token válido de Keycloak
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(org.springframework.security.config.Customizer.withDefaults()));

        return http.build();
    }

    // SOLUCIÓN AL ERROR DE INSTANCIACIÓN: Creamos el Bean decodificador reactivo explícito
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }
}