package com.ms_tracking_clientes.Servicios;

import com.ms_tracking_clientes.Models.Cliente;
import com.ms_tracking_clientes.Repositorios.ClienteOpcion;
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

    public List<ClienteOpcion> listarOpciones(){
        return repoCliente.listarOpciones();
    }

    //obtener
    public Optional <Cliente> obtenerCliente(Long idCliente){
        return repoCliente.findById(idCliente);
    }

    public Cliente editarCliente(Long idCliente, Cliente datos){
        Cliente existente = repoCliente.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no hallado " + idCliente));
        existente.setTipoCliente(datos.getTipoCliente());
        existente.setTipoDoc(datos.getTipoDoc());
        existente.setDocumento(datos.getDocumento());
        existente.setNombres(datos.getNombres());
        existente.setApellidos(datos.getApellidos());
        existente.setTelefono(datos.getTelefono());
        existente.setCorreo(datos.getCorreo());
        existente.setDireccion(datos.getDireccion());
        return repoCliente.save(existente);
    }

    public void borrarCiente(Long idCliente){
        repoCliente.deleteById(idCliente);
    }



}
