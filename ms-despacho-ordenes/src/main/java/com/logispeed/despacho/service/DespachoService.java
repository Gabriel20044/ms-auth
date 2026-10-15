package com.logispeed.despacho.service;

import com.logispeed.despacho.client.TrackingClient;
import com.logispeed.despacho.dto.ClienteDto;
import com.logispeed.despacho.dto.DespachoRequestDTO;
import com.logispeed.despacho.dto.PaqueteDto;
import com.logispeed.despacho.entity.Despacho;
import com.logispeed.despacho.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespachoService {

    @Autowired
    private DespachoRepository despachoRepository;

    @Autowired
    private TrackingClient trackingClient;

    public List<Despacho> listarTodas() {
        return despachoRepository.findAll();
    }

    public Despacho registrarDespacho(DespachoRequestDTO dto) {

        ClienteDto cliente = trackingClient.obtenerClientePorId(dto.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("El cliente con ID " + dto.getClienteId() + " no existe.");
        }


        PaqueteDto paquete = trackingClient.obtenerPaquetePorId(dto.getPaqueteId());
        if (paquete == null) {
            throw new RuntimeException("El paquete con ID " + dto.getPaqueteId() + " no existe.");
        }


        Despacho despacho = new Despacho();
        despacho.setCodigoDespacho(dto.getCodigoDespacho());
        despacho.setClienteId(dto.getClienteId());
        despacho.setPaqueteId(dto.getPaqueteId());
        despacho.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        despacho.setEstadoDespacho("PROCESANDO");
        despacho.setObservacion("Validación exitosa de cliente y paquete vía Feign");

        return despachoRepository.save(despacho);
    }
}