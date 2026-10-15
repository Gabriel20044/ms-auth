package com.ms_tracking_clientes.controller;

import com.ms_tracking_clientes.Models.Cliente;
import com.ms_tracking_clientes.Repositorios.ClienteOpcion;
import com.ms_tracking_clientes.Repositorios.clienteReporte;
import com.ms_tracking_clientes.Servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin
public class ClienteController {

    @Autowired
    private ClienteServicio cServicio;

    //Hacer un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> hacerCliente(@RequestBody Cliente cliente){
        return ResponseEntity.ok(cServicio.guardarCliente(cliente));
    }

    //listar clientes con el store procedure que puse en mi repo y mi servicio
    @GetMapping
    public ResponseEntity<List<clienteReporte>> listar(){
        return  ResponseEntity.ok(cServicio.listarClientes());
    }

    @GetMapping("/opciones")
    public ResponseEntity<List<ClienteOpcion>> opciones(){
        return ResponseEntity.ok(cServicio.listarOpciones());
    }


    //obtener
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Long id){
        return ResponseEntity.ok(cServicio.obtenerCliente(id)
                .orElseThrow(() -> new RuntimeException("Cliente no hallado " + id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> editar(@PathVariable Long id, @RequestBody Cliente cliente){
        return ResponseEntity.ok(cServicio.editarCliente(id, cliente));
    }

    //eliminar cliente

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> borrar(@PathVariable Long id){
        cServicio.borrarCiente(id);
        return ResponseEntity.noContent().build();
    }
}
