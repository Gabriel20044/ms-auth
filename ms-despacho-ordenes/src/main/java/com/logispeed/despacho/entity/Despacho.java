package com.logispeed.despacho.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_despacho")
@Getter @Setter
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_despacho", nullable = false, unique = true)
    private String codigoDespacho;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "paquete_id", nullable = false)
    private Long paqueteId;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_estimada_entrega")
    private LocalDateTime fechaEstimadaEntrega;

    @Column(name = "estado_despacho")
    private String estadoDespacho = "PENDIENTE";

    @Column(name = "observacion")
    private String observacion;
}