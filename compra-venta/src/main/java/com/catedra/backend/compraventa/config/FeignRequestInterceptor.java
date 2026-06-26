package com.catedra.backend.compraventa.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Interceptor de requests Feign.
 *
 * NOTA: La propagación del token JWT de Keycloak está temporalmente deshabilitada.
 * Cuando se reactive la seguridad M2M, descomentar el bloque de autenticación
 * y asegurarse de que SecurityContextHolder contenga un JwtAuthenticationToken válido.
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // Keycloak M2M deshabilitado temporalmente.
        // Sin propagación de token entre microservicios en esta fase de integración.

        // Para reactivar:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication instanceof JwtAuthenticationToken jwtAuth) {
        //     template.header("Authorization", "Bearer " + jwtAuth.getToken().getTokenValue());
        // }
    }
}
