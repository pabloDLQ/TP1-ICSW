package com.bdgs.hotel_premier.repository;

import com.bdgs.hotel_premier.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Habitacion}.
 * 
 * <p>Proporciona operaciones CRUD básicas para la gestión de habitaciones
 * del hotel en la base de datos.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
}
