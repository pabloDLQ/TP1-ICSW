package com.bdgs.hotel_premier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.Huesped;
import com.bdgs.hotel_premier.enums.TipoDocumento;
import java.util.Optional;
import com.bdgs.hotel_premier.entity.Direccion;

/**
 * Repositorio JPA para la entidad {@link Huesped}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para gestionar huéspedes,
 * incluyendo búsqueda por tipo y número de documento, y validaciones de existencia.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface HuespedRepository extends JpaRepository <Huesped, Long> {

    boolean existsByTipoDocumentoAndNumeroDocumento(
        TipoDocumento tipoDocumento,
        String numeroDocumento
    );

    Optional<Huesped> findByTipoDocumentoAndNumeroDocumento(
        TipoDocumento tipoDocumento,
        String numeroDocumento
    );

    boolean existsByDireccion(Direccion direccion);
}
