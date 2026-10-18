package com.logispeed.auth.service;

import com.logispeed.auth.config.JwtProvider;
import com.logispeed.auth.dto.AuthResponseDTO;
import com.logispeed.auth.dto.LoginRequestDTO;
import com.logispeed.auth.entity.Usuario;
import com.logispeed.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthResponseDTO login(LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Credenciales inválidas: Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getEstado()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas: Contraseña incorrecta");
        }

        String token = jwtProvider.generateToken(usuario.getUsername(), usuario.getRol().getNombre());

        return new AuthResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol().getNombre(),
                "Autenticación exitosa"
        );
    }
}