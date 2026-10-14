package com.ms_tracking_clientes.Servicios;

import com.ms_tracking_clientes.dto.AuthResponseDTO;
import com.ms_tracking_clientes.dto.LoginRequestDTO;
import com.ms_tracking_clientes.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthResponseDTO login(LoginRequestDTO request) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Credenciales invalidas: usuario no encontrado");
        }

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Credenciales invalidas: contrasena incorrecta");
        }

        // La autoridad viene como "ROLE_ADMIN"; se guarda el rol sin prefijo en el token
        String rol = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .orElse("USER");

        String token = jwtProvider.generateToken(userDetails.getUsername(), rol);

        return new AuthResponseDTO(token, userDetails.getUsername(), rol, "Autenticacion exitosa");
    }
}
