package com.logispeed.despacho.dto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDto {
    private Long id;
    private String documento;
    private String nombres;
    private String apellidos;
    private String correo;
}