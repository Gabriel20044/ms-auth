-- ==========================================================
-- SCRIPT COMPLETO: MÓDULO DE CLIENTES Y PAQUETES (LOGISPEED)
-- ==========================================================

CREATE DATABASE IF NOT EXISTS bd_logispeed_ClienteyPaquetes;
USE bd_logispeed_ClienteyPaquetes;

-- 1. CREACIÓN DE TABLAS BASE
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_persona VARCHAR(30) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paquetes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_rastreo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    estado_paquete VARCHAR(30) DEFAULT 'REGISTRADO',
    peso_en_kg DECIMAL(10,2) NOT NULL,
    id_cliente INT NOT NULL,
    fecha_pedido DATE NOT NULL,
    fecha_salida_pedido DATE NULL,
    fecha_entrega_estimada DATE NULL,
    fecha_entrega DATE NULL,
    CONSTRAINT fk_paquete_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE
);

-- 2. INSERTS DE DATOS INICIALES
INSERT INTO clientes
    (tipo_persona, tipo_documento, documento, nombres, apellidos, telefono, correo, direccion)
VALUES
    ('PERSONA_NATURAL', 'DNI',        '70012345', 'María',    'Quispe Ramos',  '987654321', 'maria.q@example.com', 'Av. Los Pinos 123, Lima'),
    ('PERSONA_NATURAL', 'DNI',        '70023456', 'Brian',   'Pfeiffer Zegarra', '987654322', 'brian.p@example.com', 'Calle Calle, Arequipa'),
    ('EMPRESA',         'RUC',        '20512345678', 'Comercial Andina SAC', '-', '987654323', 'ventas@andina.com', 'Calle Comercio 789, Cusco'),
    ('PERSONA_NATURAL', 'PASAPORTE', 'P01234567', 'Lucía',    'Fernández Paz', '987654324', 'lucia.f@example.com', 'Av. Brasil 321, Trujillo'),
    ('PERSONA_NATURAL', 'DNI',        '70034567', 'Jorge',    'Salazar Vega', '987654325', 'jorge.s@example.com', 'Prolongada Norte 654, Piura'),
    ('PERSONA_NATURAL', 'DNI',        '20523578', 'Hugo', 'Alvarez Rojas', '974654311', 'hugo.p@example.com', 'Calle Hugo 444, Talara'),
    ('EMPRESA',         'RUC',        '20714345154', 'Importaciones Costa SAC', '-', '981741433', 'ventas@importacionesCosta.com', 'Calle Las nardas 144, Huancavelica');

INSERT INTO paquetes
    (codigo_rastreo, descripcion, estado_paquete, peso_en_kg,
     id_cliente, fecha_pedido, fecha_salida_pedido, fecha_entrega_estimada, fecha_entrega)
VALUES
    ('TRK001', 'Libros escolares', 'REGISTRADO', 1.50, 1, '2026-10-12', NULL, NULL, NULL),
    ('TRK002', 'Laptop', 'EN_TRANSITO', 2.50, 2, '2026-10-12', '2026-10-12', '2026-10-19', NULL),
    ('TRK003', 'Celular', 'EN_TRANSITO', 0.30, 3, '2026-10-08', '2026-10-09', '2026-10-16', NULL),
    ('TRK004', 'Ropa', 'ENTREGADO', 1.20, 4, '2026-10-05', '2026-10-05', '2026-10-12', '2026-10-09'),
    ('TRK005', 'Electrodoméstico', 'ENTREGADO', 8.00, 5, '2026-10-01', '2026-10-01', '2026-10-08', '2026-10-09'), -- [CORREGIDO] Era un punto, ahora es coma
    ('TRK006', 'Libros escolares', 'REGISTRADO', 1.50, 1, '2026-10-11', NULL, NULL, NULL),
    ('TRK007', 'Electrodoméstico', 'ENTREGADO', 7.50, 5, '2026-09-28', '2026-09-30', '2026-10-07', '2026-10-11');

-- 3. FUNCIÓN PARA CÁLCULO DE DÍAS HÁBILES
DELIMITER //

DROP FUNCTION IF EXISTS funcion_sumar_dias_habiles//

CREATE FUNCTION funcion_sumar_dias_habiles(fecha_inicio DATE, dias INT)
RETURNS DATE DETERMINISTIC
BEGIN
  DECLARE contador INT DEFAULT 0;
  DECLARE resultado DATE DEFAULT fecha_inicio;
  WHILE contador < dias DO
    SET resultado = resultado + INTERVAL 1 DAY;
    IF DAYOFWEEK(resultado) NOT IN (1,7) THEN
      SET contador = contador + 1;
    END IF;
  END WHILE;
  RETURN resultado;
END//

-- 4. STORED PROCEDURES (PROCEDIMIENTOS ALMACENADOS)

DROP PROCEDURE IF EXISTS sp_registrar_paquete//
CREATE PROCEDURE sp_registrar_paquete(
    IN p_id_paquete BIGINT,
    IN p_fecha_salida DATE
)
BEGIN
	UPDATE paquetes
	SET estado_paquete = 'EN_TRANSITO',
	    fecha_salida_pedido = p_fecha_salida,
	    fecha_entrega_estimada = funcion_sumar_dias_habiles(p_fecha_salida, 5)
	WHERE id = p_id_paquete;
END//

DROP PROCEDURE IF EXISTS sp_registrar_entrega//
CREATE PROCEDURE sp_registrar_entrega(
    IN p_id_paquete BIGINT,
    IN p_fecha_entrega DATE
)
BEGIN
	UPDATE paquetes
	SET estado_paquete = 'ENTREGADO',
	    fecha_entrega = p_fecha_entrega
	WHERE id = p_id_paquete;
END//

DROP PROCEDURE IF EXISTS sp_paquetes_reporte//
CREATE PROCEDURE sp_paquetes_reporte()
BEGIN
	SELECT 
	    p.id,
	    p.descripcion,
	    p.codigo_rastreo,
	    p.peso_en_kg,
	    CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
	    p.fecha_pedido,
	    CASE
            WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
            ELSE 'En Progreso'
        END AS fecha_salida_pedido,
	    CASE
            WHEN p.fecha_entrega_estimada IS NULL THEN 'En Progreso'
            WHEN p.fecha_entrega IS NOT NULL AND p.fecha_entrega > p.fecha_entrega_estimada
                 THEN CONCAT(CAST(p.fecha_entrega_estimada AS CHAR), ' - PEDIDO CON RETRASO')
            ELSE CAST(p.fecha_entrega_estimada AS CHAR)
        END AS fecha_entrega_estimada,
	    CASE
			WHEN p.fecha_entrega IS NOT NULL THEN CAST(p.fecha_entrega AS CHAR)
			ELSE 'En Progreso'
		END AS fecha_entrega,
		p.estado_paquete
	FROM paquetes p 
	INNER JOIN clientes c ON p.id_cliente = c.id;
END//

DROP PROCEDURE IF EXISTS sp_listar_Clientes//
CREATE PROCEDURE sp_listar_Clientes()
BEGIN
	SELECT 
	    id,
	    CONCAT(nombres, ' ', apellidos) AS Nombre,
	    tipo_persona, 
	    tipo_documento, 
	    documento AS Documento,
	    telefono AS Telefono,
	    correo AS correo,
	    direccion AS Direccion
	FROM clientes;
END//

DROP PROCEDURE IF EXISTS sp_listar_Paquetes_Segun_Cliente//
CREATE PROCEDURE sp_listar_Paquetes_Segun_Cliente(
    IN p_id_cliente BIGINT
)
BEGIN
    SELECT 
        p.id,
        p.descripcion,
        p.codigo_rastreo,
        p.peso_en_kg,
        CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
        p.fecha_pedido,
        CASE
            WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
            ELSE 'En Progreso'
        END AS fecha_salida_pedido,
        CASE
            WHEN p.fecha_entrega_estimada IS NULL THEN 'En Progreso'
            WHEN p.fecha_entrega IS NOT NULL AND p.fecha_entrega > p.fecha_entrega_estimada
                 THEN CONCAT(CAST(p.fecha_entrega_estimada AS CHAR), ' - PEDIDO CON RETRASO')
            ELSE CAST(p.fecha_entrega_estimada AS CHAR)
        END AS fecha_entrega_estimada,
        CASE
			WHEN p.fecha_entrega IS NOT NULL THEN CAST(p.fecha_entrega AS CHAR)
			ELSE 'En Progreso'
		END AS fecha_entrega,
		p.estado_paquete
    FROM paquetes p 
    INNER JOIN clientes c ON p.id_cliente = c.id
    WHERE c.id = p_id_cliente;
END//

DROP PROCEDURE IF EXISTS sp_listar_Paquetes_Segun_Estado//
CREATE PROCEDURE sp_listar_Paquetes_Segun_Estado(
    IN p_estado_paquete VARCHAR(20)
)
BEGIN
    SELECT 
        p.id,
        p.descripcion,
        p.codigo_rastreo,
        p.peso_en_kg,
        CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
        p.fecha_pedido,
        CASE
            WHEN p.fecha_salida_pedido IS NOT NULL THEN CAST(p.fecha_salida_pedido AS CHAR)
            ELSE 'En Progreso'
        END AS fecha_salida_pedido,
        CASE
            WHEN p.fecha_entrega_estimada IS NULL THEN 'En Progreso'
            WHEN p.fecha_entrega IS NOT NULL AND p.fecha_entrega > p.fecha_entrega_estimada
                 THEN CONCAT(CAST(p.fecha_entrega_estimada AS CHAR), ' - PEDIDO CON RETRASO')
            ELSE CAST(p.fecha_entrega_estimada AS CHAR)
        END AS fecha_entrega_estimada,
        CASE
			WHEN p.fecha_entrega IS NOT NULL THEN CAST(p.fecha_entrega AS CHAR)
			ELSE 'En Progreso'
		END AS fecha_entrega,
		p.estado_paquete
    FROM paquetes p 
    INNER JOIN clientes c ON p.id_cliente = c.id
    WHERE p.estado_paquete = p_estado_paquete;
END//

DELIMITER ;

-- 5. PRUEBAS Y VERIFICACIONES DE FUNCIONAMIENTO
SELECT funcion_sumar_dias_habiles('2026-10-14', 5) AS prueba_dias_habiles;

CALL sp_registrar_paquete(1, '2026-10-12');
CALL sp_registrar_entrega(1, '2026-10-15');

-- Consultas generales de prueba
CALL sp_paquetes_reporte();
CALL sp_listar_Clientes();
CALL sp_listar_Paquetes_Segun_Cliente(2);
CALL sp_listar_Paquetes_Segun_Estado('ENTREGADO');