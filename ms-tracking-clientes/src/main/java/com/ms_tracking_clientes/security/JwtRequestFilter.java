package com.ms_tracking_clientes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que se ejecuta antes de la autenticacion por usuario/password.
 * Lee el header "Authorization: Bearer <token>", lo valida con JwtProvider
 * y, si es valido, marca al usuario como autenticado en el contexto de seguridad.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = extraerToken(request);

        if (token != null
                && jwtProvider.validarToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtProvider.getUsernameFromToken(token);
            String rol = jwtProvider.getRolFromToken(token);

            List<SimpleGrantedAuthority> authorities = rol != null
                    ? List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                    : List.of();

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        // Siempre se continua el filtro; la autorizacion la decide SecurityConfig
        chain.doFilter(request, response);
    }

    private String extraerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
