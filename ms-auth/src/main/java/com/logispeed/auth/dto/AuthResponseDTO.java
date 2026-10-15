package com.logispeed.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String rol;
    private String mensaje;

    public AuthResponseDTO(String token, String username, String rol, String mensaje) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.mensaje = mensaje;
    }
}