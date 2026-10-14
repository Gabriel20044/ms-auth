package com.ms_tracking_clientes.Servicios;

import com.ms_tracking_clientes.Models.Cliente;
import com.ms_tracking_clientes.Models.Paquete;
import com.ms_tracking_clientes.Models.estadoPaquete;
import com.ms_tracking_clientes.Repositorios.ClienteRepo;
import com.ms_tracking_clientes.Repositorios.PaqueteRepo;
import com.ms_tracking_clientes.Repositorios.PaqueteReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaqueteServicio {

    @Autowired
    private PaqueteRepo repoPaquete;

    @Autowired
    private ClienteRepo repoCliente;

    //guardar
public Paquete registrarNuevoPaquete(String descripcion, BigDecimal pesoKG, Long idCliente){
    Cliente cliente = repoCliente.findById(idCliente)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no valido " + idCliente));
    Paquete paquete = new Paquete();
    paquete.setDescripcion(descripcion);
    paquete.setPesoKG(pesoKG);
    paquete.setCliente(cliente);
    paquete.setEstado(estadoPaquete.REGISTRADO);
    paquete.setFechaPedido(LocalDate.now());

    Paquete paqueteGuardado = repoPaquete.save(paquete);
    paqueteGuardado.setCodigoRastreo(String.format("TRK%04d", paqueteGuardado.getId()));
    return  repoPaquete.save(paqueteGuardado);
}

public Paquete buscarSegunId(Long id){
    return repoPaquete.findById(id)
            .orElseThrow(() -> new RuntimeException("Paquete no valido " + id));
}

//REPORTES
    //REPORTE CON PAQUETE Y CLIENTES, EL REPORTE QUE SE VE EN EL FRONT LISTA
    public List<PaqueteReporte> listarPaquetes(){
    return repoPaquete.listaPaquetes();
    }

    //Pasquetes segun cliente
    public  List<PaqueteReporte> listaSegunCliente(Long idCliente){
    return repoPaquete.listarSegunCliente(idCliente);
    }

    //SEGUN ESTADO
    public List<PaqueteReporte> listaSegunEstado(String estado){
    if (estado == null   || !estado.matches("REGISTRADO|EN_TRANSITO|ENTREGADO")){
        throw  new IllegalArgumentException("Estado no valido " + estado);
        }
    return repoPaquete.listarSegunEstado(estado);
    }


    //ACCIONES PARA CAMBIAR ESTADO
        //EN CASO EL PAQUEETE YA ESTA EN RUTA.
    public  void enviarPaquete(Long idPaquete, LocalDate fechaSalida){
    repoPaquete.registrarPaquete(idPaquete, fechaSalida);
    }
        //EN CASO YA SE ENTREGO EL PAQUETE.
    public void entregarPaquete(long idPaquete, LocalDate fechaEntrega){
    repoPaquete.registrarEntrega(idPaquete,fechaEntrega);
    }
}
