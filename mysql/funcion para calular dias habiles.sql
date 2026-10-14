use bd_logispeed_ClienteyPaquetes;

-- Solo reemplaza la FUNCION; NO borra los procedimientos
-- (asi puedes volver a ejecutar este archivo sin romper la app)
DROP FUNCTION IF EXISTS funcion_sumar_dias_habiles;

delimiter $$
create function funcion_sumar_dias_habiles(fecha_inicio date, dias int)
returns date
deterministic
begin

	declare contador int default 0;
	declare resultado DATE default fecha_inicio;

	-- CORREGIDO: antes decia "contador > dias" y el bucle nunca avanzaba,
	-- por eso la fecha estimada quedaba igual a la fecha de salida.
	while contador < dias DO
		set resultado = resultado + interval 1 day;
		if DAYOFWEEK(resultado) not in (1, 7) then
			set contador = contador + 1;
		end if;
	end while;

	return resultado;
end $$
delimiter ;

-- Prueba rapida: 2026-10-14 (miercoles) + 5 dias habiles => 2026-10-21 (miercoles)
-- select funcion_sumar_dias_habiles('2026-10-14', 5);
