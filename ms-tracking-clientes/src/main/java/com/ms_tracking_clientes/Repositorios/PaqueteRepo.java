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

    //Registrar Paquete, cuando el paquete esta en transito

    @Modifying
    @Transactional
    @Query(value = "{CALL sp_registrar_paquete(:idPaquete, :fechaSalida)}", nativeQuery = true)
    void registrarPaquete(@Param("idPaquete") Long idPaquete, @Param("fechaSalida") LocalDate fechaSalida);


    //Registrar entrega real del paquete

    @Modifying
    @Transactional
    @Query(value = "{CALL sp_registrar_entrega(:idPaquete, :fechaEntrega)}", nativeQuery = true)
    void registrarEntrega(@Param("idPaquete") Long idPaquete, @Param("fechaEntrega")LocalDate fechaEntrega);

    //Reporte con los clientes
    @Query(value = "{call sp_paquetes_reporte()}", nativeQuery = true)
    List<PaqueteReporte> listaPaquetes();

    // Reporte para poner los paquetes segun cliente

    @Query(value = "{call sp_listar_Paquetes_Segun_Cliente(:idCliente)}", nativeQuery = true)
    List<PaqueteReporte> listarSegunCliente(@Param("idCliente")Long idCliente);

    // Paquete segun estado (REGISTRADO, EN_TRANSITO, ENTREGADO)
    @Query(value = "{call sp_listar_Paquetes_Segun_Estado(:estadoPaquete )}", nativeQuery = true)
    List<PaqueteReporte> listarSegunEstado(@Param("estadoPaquete")String estadoPaquete);


}
