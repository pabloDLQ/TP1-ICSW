package com.bdgs.hotel_premier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.ResponsablePago;

/**
 * Repositorio JPA base para la entidad {@link ResponsablePago}.
 * 
 * <p>Proporciona operaciones CRUD básicas para la jerarquía de responsables de pago,
 * permitiendo operaciones polimórficas sobre {@link com.bdgs.hotel_premier.entity.PersonaFisica}
 * y {@link com.bdgs.hotel_premier.entity.PersonaJuridica}.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface ResponsablePagoRepository extends JpaRepository<ResponsablePago, Long> {
    
}
