package com.bdgs.hotel_premier.repository;

import com.bdgs.hotel_premier.entity.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link TipoHabitacion}.
 * 
 * <p>Proporciona operaciones CRUD básicas para la gestión de tipos de habitación
 * en la base de datos.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
}
