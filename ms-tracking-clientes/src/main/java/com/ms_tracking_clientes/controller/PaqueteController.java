package com.ms_tracking_clientes.controller;

import com.ms_tracking_clientes.Models.Paquete;
import com.ms_tracking_clientes.Repositorios.PaqueteReporte;
import com.ms_tracking_clientes.Servicios.ClienteServicio;
import com.ms_tracking_clientes.Servicios.PaqueteServicio;
import com.ms_tracking_clientes.dto.PaqueteRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/paquete")
@CrossOrigin
public class PaqueteController {

    @Autowired
    private PaqueteServicio pServicio;

    @Autowired
    private ClienteServicio cServicio;

    //Nuevo paquete POST
    @PostMapping
    public ResponseEntity<Paquete> hacerPaquete(@RequestBody PaqueteRequestDTO dto){
        Paquete paqueteNuevo = pServicio.registrarNuevoPaquete(dto.getDescripcion(), dto.getPesoKG(), dto.getIdCliente());
        return ResponseEntity.ok(paqueteNuevo);
    }

    //Obtener Segun ID GET
    @GetMapping("/{id}")
    public ResponseEntity<Paquete> buscarPaqueteSegunId(@PathVariable Long id){
        return ResponseEntity.ok(pServicio.buscarSegunId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> editarPaquete(@PathVariable Long id, @RequestBody PaqueteRequestDTO dto){
        return ResponseEntity.ok(pServicio.editarPaquete(id, dto.getDescripcion(), dto.getPesoKG()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaquete(@PathVariable Long id){
        pServicio.eliminarPaquete(id);
        return ResponseEntity.noContent().build();
    }

    //Listar paquetes con el reporte general. Es un metodo GET que usa el store procedure que se hizo para el listado

    @GetMapping
    public ResponseEntity<List<PaqueteReporte> >listarTodosLosPaquetesConSusClientes(){
        return ResponseEntity.ok(pServicio.listarPaquetes());
    }

    //Listar Paquetes Segun Cliente GET
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PaqueteReporte>>buscarPaqueteSegunCliente(@PathVariable Long idCliente){
        return  ResponseEntity.ok(pServicio.listaSegunCliente(idCliente));
    }

    //Paquetes Segun estado GET
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PaqueteReporte>>buscarPaqueteSegunEstado(@PathVariable String estado){
        return ResponseEntity.ok(pServicio.listaSegunEstado(estado));
    }

    //Enviar paquete a ruta POST
    //En postman  /paquetes/{id}/despachar?fecha=2026-10-12

    @PostMapping("/{id}/envioARuta")
    public ResponseEntity<String> envioARuta(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fecha){
        pServicio.enviarPaquete(id, fecha);
        return ResponseEntity.ok("Paquete " + " enviado");
    }

    //Paquete Entregado PATCH
    //En postman /paquetes/{id}/entrega?fecha=2026-10-15
    @PatchMapping("/{id}/paqueteEntregado")
    public  ResponseEntity<String> paqueteEntregado(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fecha){
        pServicio.entregarPaquete(id, fecha);
        return ResponseEntity.ok("Paquete " + id + " entregado" + " en fecha: " + fecha);
    }
}
