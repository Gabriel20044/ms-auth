package com.logispeed.despacho.dto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaqueteDto {
    private Long id;
    private String codigoRastreo;
    private String descripcion;
    private String estado;
}
