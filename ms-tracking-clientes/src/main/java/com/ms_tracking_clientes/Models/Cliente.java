package com.ms_tracking_clientes.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Clientes")
public class Cliente {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "tipo_Persona", nullable = false)
        @Enumerated(EnumType.STRING)
        private tipoCliente tipoCliente; //Persona Natural o Empresa

        @Column( name = "tipo_documento", nullable = false)
        @Enumerated(EnumType.STRING)
        private tipoDocumento tipoDoc; //DNI, RUC o Pasaporte

        @Column(length = 11, nullable = false)
        private String documento;

        @Column(name = "Nombres", length = 200, nullable = false)
        private String nombres;

        @Column(name = "Apellidos", length = 200, nullable = false)
        private  String apellidos;

        @Column (length = 9, nullable = false)
        private  String telefono;

        @Column(name = "Correo")
        private String correo;

        @Column (name = "Direccion", length = 250)
        private String direccion;

        @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Paquete> paquetes = new ArrayList<>();

        public Cliente() {
        }

        public Cliente(Long id, tipoCliente tipoCliente, tipoDocumento tipoDoc, String documento, String nombres, String apellidos, String telefono, String correo, String direccion, List<Paquete> paquetes) {
            this.id = id;
            this.tipoCliente = tipoCliente;
            this.tipoDoc = tipoDoc;
            this.documento = documento;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.telefono = telefono;
            this.correo = correo;
            this.direccion = direccion;
            this.paquetes = paquetes;
        }

    }


