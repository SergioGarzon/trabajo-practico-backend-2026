package utn.frc.edu.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Desactivamos CSRF ya que nos manejamos con tokens sin estado (stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Si tenés algún endpoint que deba ser público, lo declarás acá:
                        // .pathMatchers("/api/public/**").permitAll()
                        //.pathMatchers("/usuarios").permitAll()
                        // Cualquier otra petición que pase por el gateway exige token válido
                        .anyExchange().authenticated()
                )
                // Configura al Gateway para que actúe como Resource Server validando JWTs
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(org.springframework.security.config.Customizer.withDefaults()));

        return http.build();
    }
}