package com.pruebatecnica.inventory_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String API_KEY_VALUE = "SECRET123"; // En producción, esto iría en application.yml

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el header
        String apiKey = request.getHeader(API_KEY_HEADER);

        // Validar
        if (API_KEY_VALUE.equals(apiKey)) {
            // Si es válido, creamos una autenticación "fake" para que Spring Security lo deje pasar
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken("API_USER", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Si es inválido, devolvemos 401 Unauthorized y cortamos la petición
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("API Key invalida o ausente");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * CRUCIAL: Este método evita que el filtro se ejecute en las rutas de Swagger.
     * Esto soluciona el error 500 y "Failed to load API definition".
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html");
    }
}
