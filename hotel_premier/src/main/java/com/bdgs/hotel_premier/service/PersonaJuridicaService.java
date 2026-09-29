package com.bdgs.hotel_premier.service;

import org.springframework.stereotype.Service;

import com.bdgs.hotel_premier.entity.PersonaJuridica;
import com.bdgs.hotel_premier.repository.PersonaJuridicaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con PersonaJuridica (responsables de pago jurídicos).
 * 
 * <p>Una PersonaJuridica representa a una empresa o entidad legal que actúa como responsable
 * de pago de una factura. Este servicio maneja la búsqueda y validación de estas entidades,
 * asegurando que existan en el sistema antes de utilizarlas en operaciones de facturación.</p>
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Buscar personas jurídicas existentes por CUIT</li>
 *   <li>Validar existencia de personas jurídicas</li>
 *   <li>Proporcionar excepciones descriptivas cuando no se encuentran</li>
 * </ul>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class PersonaJuridicaService {
    private final PersonaJuridicaRepository personaJuridicaRepository;

    public PersonaJuridicaService(PersonaJuridicaRepository personaJuridicaRepository) {
        this.personaJuridicaRepository = personaJuridicaRepository;
    }

    /**
     * Busca una PersonaJuridica por su CUIT.
     * 
     * @param cuit CUIT de la persona jurídica
     * @return Optional con la PersonaJuridica si existe
     */
    public Optional<PersonaJuridica> buscarPorCuit(String cuit) {
        return personaJuridicaRepository.findByCuit(cuit);
    }

    /**
     * Obtiene una PersonaJuridica por su CUIT, lanzando excepción si no existe.
     * 
     * @param cuit CUIT de la persona jurídica
     * @return PersonaJuridica encontrada
     * @throws NoSuchElementException si no existe la persona jurídica con ese CUIT
     */
    public PersonaJuridica obtenerPorCuit(String cuit) {
        return personaJuridicaRepository.findByCuit(cuit)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró persona jurídica con CUIT: " + cuit + 
                        ". La persona jurídica debe estar registrada en el sistema."));
    }

    /**
     * Verifica si existe una PersonaJuridica con el CUIT dado.
     * 
     * @param cuit CUIT a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorCuit(String cuit) {
        return personaJuridicaRepository.findByCuit(cuit).isPresent();
    }
}
