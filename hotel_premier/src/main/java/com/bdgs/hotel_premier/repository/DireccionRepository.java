package com.bdgs.hotel_premier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bdgs.hotel_premier.entity.Direccion;

/**
 * Repositorio JPA para la entidad {@link Direccion}.
 * 
 * <p>Proporciona operaciones CRUD y consultas para gestionar direcciones,
 * incluyendo búsqueda de direcciones exactas comparando todos sus campos.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    Direccion findByCalleAndNumeroAndNroDepartamentoAndPisoAndCodigoPostalAndLocalidadAndProvinciaAndPais(
            String calle,
            String numero,
            String nroDepartamento,
            String piso,
            String codigoPostal,
            String localidad,
            String provincia,
            String pais
    );
}
