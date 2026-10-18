-- ==========================================================
-- FUNCIÓN: CÁLCULO DE DÍAS HÁBILES (LOGISPEED)
-- ==========================================================

USE bd_logispeed_ClienteyPaquetes;

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

DELIMITER ;

-- Prueba rápida: 2026-10-14 (miércoles) + 5 días hábiles => 2026-10-21 (miércoles)
SELECT funcion_sumar_dias_habiles('2026-10-14', 5) AS prueba_dias_habiles;
