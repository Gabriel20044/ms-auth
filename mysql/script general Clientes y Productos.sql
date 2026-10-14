create database bd_logispeed_ClienteyPaquetes
use bd_logispeed_ClienteyPaquetes
 -- drop table Clientes
 -- drop table paquetes

INSERT INTO clientes
    (tipo_persona, tipo_documento, documento, nombres, apellidos, telefono, correo, direccion)
VALUES
    ('PERSONA_NATURAL', 'DNI',       '70012345', 'María',    'Quispe Ramos',  '987654321', 'maria.q@example.com', 'Av. Los Pinos 123, Lima'),
    ('PERSONA_NATURAL', 'DNI',       '70023456', 'Brian',   'Pfeiffer Zegarra', '987654322', 'brian.p@example.com', 'Calle Calle, Arequipa'),
    ('EMPRESA',         'RUC',       '20512345678', 'Comercial Andina SAC', '-', '987654323', 'ventas@andina.com', 'Calle Comercio 789, Cusco'),
    ('PERSONA_NATURAL', 'PASAPORTE', 'P01234567', 'Lucía',   'Fernández Paz', '987654324', 'lucia.f@example.com', 'Av. Brasil 321, Trujillo'),
    ('PERSONA_NATURAL', 'DNI',       '70034567', 'Jorge',    'Salazar Vega', '987654325', 'jorge.s@example.com', 'Prolongada Norte 654, Piura'),
 ('PERSONA_NATURAL',         'DNI',       '20523578', 'Hugo', 'Alvarez Rojas', '974654311', 'hugo.p@example.com', 'Calle Hugo 444, Talara'),
  ('EMPRESA',         'RUC',       '20714345154', 'Importaciones Costa SAC', '-', '981741433', 'ventas@importacionesCosta.com', 'Calle Las nardas 144, Huancavelica')
  
  
INSERT INTO paquetes
    (codigo_rastreo, descripcion, estado_paquete, peso_en_kg,
     id_cliente, fecha_pedido, fecha_salida_pedido, fecha_entrega_estimada, fecha_entrega)
VALUES
-- 1) REGISTRADO: no salió aún -> salida, estimada y entrega en NULL, SIRVE PAAA PRUEBAS
    ('TRK001', 'Libros escolares', 'REGISTRADO', 1.50,
     1, '2026-10-12', NULL, NULL, NULL),

-- 2) EN_TRANSITO: salió lunes 12-oct, estimada +5 habiles = 19-oct, entrega NULL
    ('TRK002', 'Laptop', 'EN_TRANSITO', 2.50,
     2, '2026-10-12', '2026-10-12', '2026-10-19', NULL),

-- 3) EN_TRANSITO: salió viernes 09-oct, estimada +5 habiles = 16-oct, entrega NULL
    ('TRK003', 'Celular', 'EN_TRANSITO', 0.30,
     3, '2026-10-08', '2026-10-09', '2026-10-16', NULL),


-- 4) ENTREGADO ADELANTADO: salió 05-oct, promesa 12-oct, llegó 09-oct (antes)
    ('TRK004', 'Ropa', 'ENTREGADO', 1.20,
     4, '2026-10-05', '2026-10-05', '2026-10-12', '2026-10-09'),

-- 5) ENTREGADO CON RETRASO: salió 01-oct, promesa 08-oct, llegó 09-oct (después)
    ('TRK005', 'Electrodoméstico', 'ENTREGADO', 8.00,
     5, '2026-10-01', '2026-10-01', '2026-10-08', '2026-10-09').

-- 6) REGISTRADO: no salió aún -> salida, estimada y entrega en NULL, SIRVE PAAA PRUEBAS
    ('TRK006', 'Libros escolares', 'REGISTRADO', 1.50,
     1, '2026-10-11', NULL, NULL, NULL),
     
  -- 7) ENTREGADO CON RETRASO: salió 01-oct, promesa 08-oct, llegó 09-oct (después)
    ('TRK007', 'Electrodoméstico', 'ENTREGADO', 7.50,
     5, '2026-09-28', '2026-09-30', '2026-10-07', '2026-10-11');
     



select * from clientes
select * from PAQUETES

truncate table clientes
Truncate table paquetes

show create table paquetes
show create function funcion_sumar_dias_habiles
SHOW PROCEDURE STATUS WHERE Db = 'bd_logispeed_ClienteyPaquetes';

update paquetes
set fecha_entrega_estimada = '2026-10-09'
where id = 5
-- PROCEDURES PARA VERIFICAR SI UN PAQAUETE ESTA EN PROCESO O ENTREGADO

-- EL PAQUETE ESTA EN DESPACHO
create procedure sp_registrar_paquete(
in p_id_paquete BIGINT,
in p_fecha_salida DATE
)
begin
	update paquetes
	set estado_paquete = 'EN_TRANSITO',
	fecha_salida_pedido = p_fecha_salida,
	fecha_entrega_estimada = funcion_sumar_dias_habiles(p_fecha_salida, 5)
	where id= p_id_paquete;
end

-- EL PAQUETE HA SIDO ENTREGADO
Create procedure sp_registrar_entrega(
in p_id_paquete BIGINT,
in p_fecha_entrega DATE
)
begin
	update PAQUETES
	set estado_paquete = 'ENTREGADO',
	fecha_entrega = p_fecha_entrega
	where id = p_id_paquete;
end

-- PARA CAMBIAR LOS CAMPOS fecha_salida_pedido, fecha_entrega_estimada y fecha_entrega a EN PROGRESO EN CASO LOS PEDIDOS NO SE ENCUENTREN EN RUTA O NO SE ENTREGUEN Y MOSTRAR EN EL FRONT



create procedure sp_paquetes_reporte()
begin
	select 
	p.id,
	p.descripcion,
	p.codigo_rastreo,
	p.peso_en_kg,
	CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
	p.fecha_pedido,
	
	-- EN CASO EL PAQUETE YA SE ENCUENTRA EN RUTA.
	CASE
           WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
           ELSE 'En Progreso'
    END AS fecha_salida_pedido,
	
		-- en caso el paquete ya tenga fecha de salida y aun se encuentre en ruta pero aun no llega
	case
        when p.fecha_entrega_estimada is null then 'En Progreso'
        when p.fecha_entrega is not null and p.fecha_entrega > p.fecha_entrega_estimada
                 then concat(cast(p.fecha_entrega_estimada as char), ' - PEDIDO CON RETRASO')
            else cast(p.fecha_entrega_estimada as char)
        end as fecha_entrega_estimada,
		
		-- en caso ya se entrego el paquete.
	case
			when p.fecha_entrega is not null then cast(p.fecha_entrega as char)
			else 'En Progreso'
	end as fecha_entrega,
		p.estado_paquete
	
	from paquetes p 
	inner join clientes c 
	on p.id_cliente = c.id;
end

-- PRUEBA CON EL PAQUETE 1 PARA VERIFICAR EL CICLO DE VIDA DE LA ENTREGAL

-- 1 El paquete salio a a reparto. El SP pone estado, salida y CALCULA la estimada (+5 hábiles)
CALL sp_registrar_paquete(1, '2026-10-12');

-- 3) El paquete llego antes de lo que se puso en fecha_entrega_estimada
CALL sp_registrar_entrega(1, '2026-10-15');

-- 4) Ver cómo quedó en el reporte
CALL sp_paquetes_reporte();

call sp_paquetes_reporte()

select 
	p.id,
	p.descripcion,
	p.codigo_rastreo,
	p.peso_en_kg,
	CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
	p.fecha_pedido,
	
	-- EN CASO EL PAQUETE YA SE ENCUENTRA EN RUTA.
	CASE
           WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
           ELSE 'En Progreso'
    END AS fecha_salida_pedido,
	
		-- en caso el paquete ya tenga fecha de salida y aun se encuentre en ruta pero aun no llega
	case
        when p.fecha_entrega_estimada is null then 'En Progreso'
        when p.fecha_entrega is not null and p.fecha_entrega > p.fecha_entrega_estimada
                 then concat(cast(p.fecha_entrega_estimada as char), ' - PEDIDO CON RETRASO')
            else cast(p.fecha_entrega_estimada as char)
        end as fecha_entrega_estimada,
	
		
		-- en caso ya se entrego el paquete.
	case
			when p.fecha_entrega is not null then cast(p.fecha_entrega as char)
			else 'En Progreso'
	end as fecha_entrega,
	p.estado_paquete
	
	from paquetes p 
	inner join clientes c 
	on p.id_cliente = c.id
	where c.id = 1

	-- PROCEDURE PARA LISTAR CLIENTES Y PONERLO ASI EN EL FRONT
create procedure sp_listar_Clientes()
begin
	select 
	concat(nombres,  ' ',  apellidos) as Nombre,
	tipo_persona, 
	tipo_documento, 
	documento as Documento,
	telefono as Telefono,
	correo as correo,
	direccion as Direccion
	from clientes;
 end

 call sp_listar_Clientes()

 
 -- PROCEDURE PARA LISTAR PAQUETE SEGUN LOS CLIENTES.
 create procedure sp_listar_Paquetes_Segun_Cliente(
 in p_id_cliente BIGINT
 )
 begin
select 
	p.id,
	p.descripcion,
	p.codigo_rastreo,
	p.peso_en_kg,
	CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
	p.fecha_pedido,
	-- EN CASO EL PAQUETE YA SE ENCUENTRA EN RUTA.
	CASE
           WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
           ELSE 'En Progreso'
    END AS fecha_salida_pedido,
	
		-- en caso el paquete ya tenga fecha de salida y aun se encuentre en ruta pero aun no llega
	case
        when p.fecha_entrega_estimada is null then 'En Progreso'
        when p.fecha_entrega is not null and p.fecha_entrega > p.fecha_entrega_estimada
                 then concat(cast(p.fecha_entrega_estimada as char), ' - PEDIDO CON RETRASO')
            else cast(p.fecha_entrega_estimada as char)
        end as fecha_entrega_estimada,
	
		
		-- en caso ya se entrego el paquete.
	case
			when p.fecha_entrega is not null then cast(p.fecha_entrega as char)
			else 'En Progreso'
	end as fecha_entrega,
	p.estado_paquete
	
	from paquetes p 
	inner join clientes c 
	on p.id_cliente = c.id
	where c.id = p_id_cliente;
 end
 
 	
  create procedure sp_listar_Paquetes_Segun_Estado(
 in p_estado_paquete varchar(20)
 )
 begin
select 
	p.id,
	p.descripcion,
	p.codigo_rastreo,
	p.peso_en_kg,
	CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
	p.fecha_pedido,
	-- EN CASO EL PAQUETE YA SE ENCUENTRA EN RUTA.
	CASE
           WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
           ELSE 'En Progreso'
    END AS fecha_salida_pedido,
		-- en caso el paquete ya tenga fecha de salida y aun se encuentre en ruta pero aun no llega
	case
        when p.fecha_entrega_estimada is null then 'En Progreso'
        when p.fecha_entrega is not null and p.fecha_entrega > p.fecha_entrega_estimada
                 then concat(cast(p.fecha_entrega_estimada as char), ' - PEDIDO CON RETRASO')
            else cast(p.fecha_entrega_estimada as char)
        end as fecha_entrega_estimada,
		-- en caso ya se entrego el paquete.
	case
			when p.fecha_entrega is not null then cast(p.fecha_entrega as char)
			else 'En Progreso'
	end as fecha_entrega,
	p.estado_paquete
	from paquetes p 
	inner join clientes c 
	on p.id_cliente = c.id
	where p.estado_paquete = p_estado_paquete;
	end
 
	
 
  call sp_listar_Paquetes_Segun_Cliente(2)
 
  call sp_listar_Paquetes_Segun_Estado('ENTREGADO')
--  drop procedure sp_listar_Paquetes_Segun_Estado
-- drop procedure sp_listar_Pedidos_Segun_Cliente