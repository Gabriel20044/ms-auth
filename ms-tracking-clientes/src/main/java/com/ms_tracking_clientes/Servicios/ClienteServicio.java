package com.ms_tracking_clientes.Servicios;

import com.ms_tracking_clientes.Models.Cliente;
import com.ms_tracking_clientes.Repositorios.ClienteRepo;
import com.ms_tracking_clientes.Repositorios.clienteReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServicio {

    @Autowired
private ClienteRepo repoCliente;

    //guardar cliente
    public Cliente guardarCliente(Cliente cliente){
        return repoCliente.save(cliente);
    }

    //listar cliente, aca no se va a usar el clasico select, se va a usar el stored procedure
    public List<clienteReporte> listarClientes(){
        return repoCliente.listaClientes();
    }

    //obtener
    public Optional <Cliente> obtenerCliente(Long idCliente){
        return repoCliente.findById(idCliente);
    }

    public void borrarCiente(Long idCliente){
        repoCliente.deleteById(idCliente);
    }



}
