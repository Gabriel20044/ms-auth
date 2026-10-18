package com.logispeed.auth.init;

import com.logispeed.auth.entity.Rol;
import com.logispeed.auth.entity.Usuario;
import com.logispeed.auth.repository.RolRepository;
import com.logispeed.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Verificar o crear el Rol Admin
        Rol rolAdmin = rolRepository.findByNombre("ROL_ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol(null, "ROL_ADMIN")));


        // 2. Buscar si el usuario ya existe o crear uno nuevo
        Usuario usuario = usuarioRepository.findByUsername("admin_logispeed")
                .orElse(new Usuario());


        // 3. Asignar los datos y ENCRIPTAR la contraseña dinámicamente
        usuario.setUsername("admin_logispeed");
        usuario.setPassword(passwordEncoder.encode("Admin2026*")); // BCrypt propio de Spring
        usuario.setEmail("admin@logispeed.com");
        usuario.setEstado(true);
        usuario.setRol(rolAdmin);


        // 4. Guardar en MySQL
        usuarioRepository.save(usuario);

        System.out.println("=================================================");
        System.out.println(">>> USUARIOS E INICIALIZACIÓN DE BCRYPT OK <<<");
        System.out.println("=================================================");
    }
}