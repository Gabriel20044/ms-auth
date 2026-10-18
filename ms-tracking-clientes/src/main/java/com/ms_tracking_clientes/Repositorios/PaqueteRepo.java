package com.ms_tracking_clientes.Repositorios;

import com.ms_tracking_clientes.Models.Paquete;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaqueteRepo extends JpaRepository<Paquete, Long> {

    @Modifying
    @Transactional
    @Query(value = "{CALL sp_registrar_paquete(:idPaquete, :fechaSalida)}", nativeQuery = true)
    void registrarPaquete(@Param("idPaquete") Long idPaquete, @Param("fechaSalida") LocalDate fechaSalida);

    @Modifying
    @Transactional
    @Query(value = "{CALL sp_registrar_entrega(:idPaquete, :fechaEntrega)}", nativeQuery = true)
    void registrarEntrega(@Param("idPaquete") Long idPaquete, @Param("fechaEntrega")LocalDate fechaEntrega);

    @Query(value = "{call sp_paquetes_reporte()}", nativeQuery = true)
    List<PaqueteReporte> listaPaquetes();

    @Query(value = "{call sp_listar_Paquetes_Segun_Cliente(:idCliente)}", nativeQuery = true)
    List<PaqueteReporte> listarSegunCliente(@Param("idCliente")Long idCliente);

    @Query(value = "{call sp_listar_Paquetes_Segun_Estado(:estadoPaquete )}", nativeQuery = true)
    List<PaqueteReporte> listarSegunEstado(@Param("estadoPaquete")String estadoPaquete);


}
