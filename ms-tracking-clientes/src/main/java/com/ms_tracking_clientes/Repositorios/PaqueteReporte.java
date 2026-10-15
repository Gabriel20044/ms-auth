package com.ms_tracking_clientes.Repositorios;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaqueteReporte {
     Long getId();
     String getDescripcion();
     String getCodigoRastreo();
     BigDecimal getPesoEnKg();
     String getCliente();
     LocalDate getFechaPedido();
     String getFechaSalidaPedido();
     String getFechaEntregaEstimada();
     String getFechaEntrega();
     String getEstadoPaquete();
}
