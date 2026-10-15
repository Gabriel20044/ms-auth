package com.logispeed.despacho.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class DespachoRequestDTO {
    private String codigoDespacho;
    private Long clienteId;
    private Long paqueteId;
    private LocalDateTime fechaEstimadaEntrega;
    private String observacion;
}