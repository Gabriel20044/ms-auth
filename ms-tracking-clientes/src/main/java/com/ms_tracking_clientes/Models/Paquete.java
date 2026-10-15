package com.ms_tracking_clientes.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "Paquetes")
public class Paquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(length = 50)
    private String codigoRastreo;

    private String descripcion;

    @Column (name = "peso_En_KG", precision = 10, scale = 2)
    private BigDecimal pesoKG;

    @Column(name = "estado_paquete")
    @Enumerated(EnumType.STRING)
    private estadoPaquete estado;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Column(name = "fecha_salida_pedido")
    private LocalDate fechaSalidaPedido;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonBackReference
    private Cliente cliente;


    public Paquete() {
    }

    public Paquete(Long id, String codigoRastreo, String descripcion, BigDecimal pesoKG, estadoPaquete estado, LocalDate fechaPedido, LocalDate fechaSalidaPedido, LocalDate fechaEntregaEstimada, LocalDate fechaEntrega, Cliente cliente ) {
        this.id = id;
        this.codigoRastreo = codigoRastreo;
        this.descripcion = descripcion;
        this.pesoKG = pesoKG;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.fechaSalidaPedido = fechaSalidaPedido;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.fechaEntrega = fechaEntrega;

        this.cliente = cliente;

    }


}
