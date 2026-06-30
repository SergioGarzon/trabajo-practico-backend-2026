package com.utnfrc.usuario_portfolios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/billetera/").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/ventas").permitAll()
                        // Cualquier otra petición (como ingresar dinero) exige token válido
                        .anyRequest().authenticated()
                )
                // Activamos la lectura de tokens JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(org.springframework.security.config.Customizer.withDefaults()));

        return http.build();
    }
}