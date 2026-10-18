package com.ms_tracking_clientes.dto;

import java.math.BigDecimal;

public class PaqueteRequestDTO {
    private String descripcion;
    private BigDecimal pesoKG;
    private Long idCliente;

    public PaqueteRequestDTO(){
    }

    public PaqueteRequestDTO(String descripcion, BigDecimal pesoKG, Long idCliente) {
        this.descripcion = descripcion;
        this.pesoKG = pesoKG;
        this.idCliente = idCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPesoKG() {
        return pesoKG;
    }

    public void setPesoKG(BigDecimal pesoKG) {
        this.pesoKG = pesoKG;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}
