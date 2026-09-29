package com.bdgs.hotel_premier.repository;

import com.bdgs.hotel_premier.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Estadia}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para gestionar estadías,
 * incluyendo búsqueda de estadías superpuestas en un rango de fechas.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {

	@Query("""
		SELECT e FROM Estadia e
		JOIN FETCH e.habitacion h
		WHERE e.fechaInicio < :fin AND e.fechaFin > :inicio
	""")
	List<Estadia> findEstadiasPorRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    Optional<Estadia> findByHabitacionIdAndFechaFin(Long idHabitacion, LocalDate fechaFin);
}
