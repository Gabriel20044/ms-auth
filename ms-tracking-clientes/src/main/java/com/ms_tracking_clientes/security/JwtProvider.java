package com.ms_tracking_clientes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtProvider AUTONOMO de ms-tracking-clientes.
 *
 * Este microservicio tiene su propio secreto, asi que AQUI SI emite y valida
 * sus propios tokens. NO tiene relacion con ms-auth: un token de ms-auth
 * (firmado con otro secreto) no pasara la validacion.
 */
@Component
public class JwtProvider {

    // Secreto PROPIO de tracking (distinto al de ms-auth)
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> parsear(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    public boolean validarToken(String token) {
        try {
            parsear(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return parsear(token).getBody().getSubject();
    }

    public String getRolFromToken(String token) {
        Object rol = parsear(token).getBody().get("rol");
        return rol == null ? null : rol.toString();
    }
}
