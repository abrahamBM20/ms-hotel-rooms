package com.example.mshotelrooms.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Exige la cabecera X-Secret-Gateway que inyecta el API Gateway (o el BFF, que
 * actúa como capa añadida de seguridad entre el gateway y este microservicio).
 *
 * Nadie puede saltarse el API Gateway para llamar a ms-hotel-rooms directamente.
 */
@Component
public class SecretGatewayFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecretGatewayFilter.class);

    @Value("${app.gateway.secret}")
    private String expectedSecret;

    /**
     * Excepción: /api/health, para que el orquestador (Docker HEALTHCHECK, ECS,
     * el balanceador) pueda consultar el estado del contenedor sin conocer el secreto.
     * Es la misma excepción que aplica el BFF.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/api/health".equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String secretHeader = request.getHeader("X-Secret-Gateway");
        String path = request.getRequestURI();

        // LOG DE DEPURACIÓN TEMPORAL
        // Muestra el secreto de la pasarela para acordarse de él mientras se
        // configura el stack. Es una entrega de clase: en producción esta línea
        // se borra, porque escribe el secreto en claro en los logs.
        System.out.println("DEBUG SECRETO -> Ruta: " + path + " | Recibido: [" + secretHeader + "] | Esperado: [" + expectedSecret + "]");

        // Validar que la cabecera exista y coincida exactamente con el secreto
        if (expectedSecret == null || !expectedSecret.equals(secretHeader)) {
            log.warn("Acceso denegado en {} {}: secreto de pasarela inválido o ausente",
                    request.getMethod(), request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Acceso denegado: Secreto de pasarela inválido o ausente");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
