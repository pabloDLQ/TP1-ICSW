package com.bdgs.hotel_premier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.Factura;

/**
 * Repositorio JPA para la entidad {@link Factura}.
 * 
 * <p>Proporciona operaciones CRUD básicas para la gestión de facturas
 * en la base de datos.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
}
