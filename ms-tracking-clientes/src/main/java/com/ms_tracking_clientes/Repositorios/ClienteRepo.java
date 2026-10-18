package com.ms_tracking_clientes.Repositorios;

import com.ms_tracking_clientes.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepo extends JpaRepository<Cliente, Long> {

    @Query(value = "{CALL sp_listar_Clientes()}", nativeQuery = true)
    List<clienteReporte> listaClientes();

    @Query("SELECT c.id AS id, CONCAT(c.nombres, ' ', c.apellidos) AS nombre FROM Cliente c ORDER BY c.id")
    List<ClienteOpcion> listarOpciones();
}
