package com.bdgs.hotel_premier.service;

import org.springframework.stereotype.Service;

import com.bdgs.hotel_premier.entity.Huesped;
import com.bdgs.hotel_premier.entity.PersonaFisica;
import com.bdgs.hotel_premier.repository.PersonaFisicaRepository;

import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con PersonaFisica (responsables de pago físicos).
 * 
 * <p>Una PersonaFisica representa a un huésped que actúa como responsable de pago de una factura.
 * Este servicio maneja la creación y búsqueda de estas entidades, vinculándolas con los huéspedes
 * registrados en el sistema.</p>
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Buscar personas físicas existentes por ID de huésped</li>
 *   <li>Crear nuevas personas físicas asociadas a un huésped</li>
 *   <li>Obtener o crear persona física según disponibilidad</li>
 * </ul>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class PersonaFisicaService {
    private final PersonaFisicaRepository personaFisicaRepository;

    public PersonaFisicaService(PersonaFisicaRepository personaFisicaRepository) {
        this.personaFisicaRepository = personaFisicaRepository;
    }

    /**
     * Busca una PersonaFisica por el ID del huésped asociado.
     * 
     * @param huespedId ID del huésped
     * @return Optional con la PersonaFisica si existe
     */
    public Optional<PersonaFisica> buscarPorHuespedId(Long huespedId) {
        return personaFisicaRepository.findByHuespedId(huespedId);
    }

    /**
     * Obtiene una PersonaFisica existente o crea una nueva si no existe.
     * 
     * @param huesped Huésped al que se asociará la PersonaFisica
     * @return PersonaFisica existente o recién creada
     */
    public PersonaFisica obtenerOCrear(Huesped huesped) {
        return personaFisicaRepository.findByHuespedId(huesped.getId())
                .orElseGet(() -> {
                    PersonaFisica nueva = new PersonaFisica();
                    nueva.setHuesped(huesped);
                    return personaFisicaRepository.save(nueva);
                });
    }

    /**
     * Crea una nueva PersonaFisica asociada a un huésped.
     * 
     * @param huesped Huésped al que se asociará la PersonaFisica
     * @return PersonaFisica creada y persistida
     */
    public PersonaFisica crear(Huesped huesped) {
        PersonaFisica nueva = new PersonaFisica();
        nueva.setHuesped(huesped);
        return personaFisicaRepository.save(nueva);
    }
}
