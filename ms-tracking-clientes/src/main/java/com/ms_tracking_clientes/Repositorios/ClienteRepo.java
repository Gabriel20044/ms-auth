package com.ms_tracking_clientes.Repositorios;

import com.ms_tracking_clientes.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepo extends JpaRepository<Cliente, Long> {

    //LISTAR CLIENTES PARA MOSTRAR EN EL FRONT
    @Query(value = "{CALL sp_listar_Clientes()}", nativeQuery = true)
    List<clienteReporte> listaClientes();
}
