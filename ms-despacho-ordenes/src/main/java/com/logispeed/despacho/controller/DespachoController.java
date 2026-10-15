package com.logispeed.despacho.controller;

import com.logispeed.despacho.dto.DespachoRequestDTO;
import com.logispeed.despacho.entity.Despacho;
import com.logispeed.despacho.service.DespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    @GetMapping
    public ResponseEntity<List<Despacho>> listar() {
        return ResponseEntity.ok(despachoService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody DespachoRequestDTO dto) {
        try {
            Despacho nuevoDespacho = despachoService.registrarDespacho(dto);
            return ResponseEntity.ok(nuevoDespacho);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar despacho: " + e.getMessage());
        }
    }
}