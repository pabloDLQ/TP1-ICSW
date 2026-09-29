package com.bdgs.hotel_premier.repository;

import com.bdgs.hotel_premier.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Reserva}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para gestionar reservas,
 * incluyendo búsqueda de reservas superpuestas en un rango de fechas y búsqueda
 * por datos específicos del huésped.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {

	@Query("""
		SELECT r FROM Reserva r
		JOIN FETCH r.habitacion h
		WHERE r.fechaInicio < :fin AND r.fechaFin > :inicio
	""")
	List<Reserva> findReservasPorRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
	
	/**
	 * Encuentra una reserva por nombre, apellido, fechas e idHabitacion exactos.
	 * Útil para identificar la reserva a cancelar.
	 */
	Reserva findByNombreAndApellidoAndFechaInicioAndFechaFinAndHabitacionId(
		String nombre, 
		String apellido, 
		LocalDate fechaInicio, 
		LocalDate fechaFin,
		Long habitacionId
	);
}
