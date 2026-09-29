package com.bdgs.hotel_premier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bdgs.hotel_premier.entity.PersonaFisica;

/**
 * Repositorio JPA para la entidad {@link PersonaFisica}.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para personas físicas
 * que actúan como responsables de pago.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Repository
public interface PersonaFisicaRepository extends JpaRepository<PersonaFisica, Long> {
    /**
     * Busca una persona física asociada a un huésped específico.
     * 
     * @param huespedId ID del huésped
     * @return Optional con la PersonaFisica si existe
     */
    Optional<PersonaFisica> findByHuespedId(Long huespedId);
}
