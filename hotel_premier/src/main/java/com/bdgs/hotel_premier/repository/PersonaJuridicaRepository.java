package com.bdgs.hotel_premier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.PersonaJuridica;

/**
 * Repositorio JPA para la entidad {@link PersonaJuridica}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para personas jurídicas
 * que actúan como responsables de pago.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica, Long> {
    /**
     * Busca una persona jurídica por su CUIT.
     * 
     * @param cuit CUIT de la persona jurídica
     * @return Optional con la PersonaJuridica si existe
     */
    Optional<PersonaJuridica> findByCuit(String cuit);
}
