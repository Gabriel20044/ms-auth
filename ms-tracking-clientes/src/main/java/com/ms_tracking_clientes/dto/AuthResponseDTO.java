package com.ms_tracking_clientes.dto;

public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String rol;
    private String mensaje;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String username, String rol, String mensaje) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
