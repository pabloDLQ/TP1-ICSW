package com.bdgs.hotel_premier.repository;

import com.bdgs.hotel_premier.entity.NoDisponibilidadHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link NoDisponibilidadHabitacion}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para gestionar
 * períodos de no disponibilidad de habitaciones, incluyendo búsqueda de
 * indisponibilidades superpuestas en un rango de fechas.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface NoDisponibilidadHabitacionRepository extends JpaRepository<NoDisponibilidadHabitacion, Long> {

    @Query("""
        SELECT n FROM NoDisponibilidadHabitacion n
        JOIN FETCH n.habitacion h
        WHERE n.fechaInicio < :fin AND n.fechaFin > :inicio
    """)
    List<NoDisponibilidadHabitacion> findNoDisponibilidadesSuperpuestas(@Param("inicio") LocalDate inicio,
                                                                       @Param("fin") LocalDate fin);
}
