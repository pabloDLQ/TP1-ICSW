package com.bdgs.hotel_premier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.GastoAdicional;

/**
 * Repositorio JPA para la entidad {@link GastoAdicional}.
 * 
 * <p>Proporciona operaciones CRUD básicas para la gestión de gastos adicionales
 * asociados a las estadías en la base de datos.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface GastoAdicionalRepository extends JpaRepository<GastoAdicional, Long> {
}
