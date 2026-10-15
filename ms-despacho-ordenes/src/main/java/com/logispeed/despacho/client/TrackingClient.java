package com.logispeed.despacho.client;

import com.logispeed.despacho.dto.ClienteDto;
import com.logispeed.despacho.dto.PaqueteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-tracking-clientes")
public interface TrackingClient {

    @GetMapping("/api/clientes/{id}")
    ClienteDto obtenerClientePorId(@PathVariable("id") Long id);

    @GetMapping("/api/paquetes/{id}")
    PaqueteDto obtenerPaquetePorId(@PathVariable("id") Long id);
}