package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.EstadiaConsultaDto;
import com.bdgs.hotel_premier.entity.Estadia;
import com.bdgs.hotel_premier.repository.EstadiaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Servicio para la gestión de estadías en el hotel.
 * 
 * <p>Proporciona operaciones para guardar, consultar y obtener estadías,
 * incluyendo búsqueda de estadías superpuestas en rangos de fechas.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class EstadiaService {

	private final EstadiaRepository estadiaRepository;

    public EstadiaService(EstadiaRepository estadiaRepository) {
        this.estadiaRepository = estadiaRepository;
    }

    public Estadia guardarEstadia(Estadia estadia) {
        return estadiaRepository.save(estadia);
    }
	
	/**
	 * Obtiene las estadías que se superponen con el rango [inicio, finExclusivo).
	 */
	public List<Estadia> obtenerEstadiasPorRango(LocalDate inicio, LocalDate finExclusivo) {
		return estadiaRepository.findEstadiasPorRango(inicio, finExclusivo);
	}

	/**
	 * Devuelve, por habitación, el conjunto de días ocupados dentro de [inicio, finExclusivo).
	 * Fin exclusivo para checkout.
	 */
	public Map<Long, Set<LocalDate>> obtenerDiasOcupadosPorHabitacion(LocalDate inicio, LocalDate finExclusivo) {
		List<Estadia> estadias = obtenerEstadiasPorRango(inicio, finExclusivo);
		Map<Long, Set<LocalDate>> mapa = new HashMap<>();

		for (Estadia e : estadias) {
			Long habId = e.getHabitacion().getId();
			Set<LocalDate> dias = mapa.computeIfAbsent(habId, k -> new HashSet<>());
			agregarDiasSolapados(dias, e.getFechaInicio(), e.getFechaFin(), inicio, finExclusivo);
		}

		return mapa;
	}

	/**
	 * Método utilitario para agregar días solapados entre un rango de entidad y un rango de consulta.
	 * Evita duplicar la lógica de cálculo de intersección de fechas.
	 * CORREGIDO: Maneja correctamente fechas inclusivas de las entidades.
	 */
	private void agregarDiasSolapados(Set<LocalDate> destino, LocalDate inicioEntidad, LocalDate finEntidad, 
								  LocalDate inicioConsulta, LocalDate finExclusivoConsulta) {
		LocalDate desde = inicioEntidad.isBefore(inicioConsulta) ? inicioConsulta : inicioEntidad;
		// Convertir finEntidad (inclusiva) a exclusiva para el cálculo
		LocalDate finEntidadExclusiva = finEntidad.plusDays(1);
		LocalDate hastaEx = finEntidadExclusiva.isAfter(finExclusivoConsulta) ? finExclusivoConsulta : finEntidadExclusiva;
		for (LocalDate d = desde; d.isBefore(hastaEx); d = d.plusDays(1)) {
			destino.add(d);
		}
	}

	public Estadia obtenerEstadiaPorIdHabYFechaFin(EstadiaConsultaDto consulta) {
		// Lógica para obtener la estadía según el ID de la habitación y la fecha de fin
		Optional<Estadia> estadiaOpt = estadiaRepository.findByHabitacionIdAndFechaFin(
				consulta.getIdHabitacion(), consulta.getFechaFin());
		
		if (estadiaOpt.isPresent()) {
			Estadia estadia = estadiaOpt.get();
			return estadia;
					
		} else {
			throw new NoSuchElementException("No se encontró una estadía para la habitación y fecha proporcionadas.");
		}
	}

	/**
	 * Obtiene una estadía por su ID.
	 * 
	 * @param estadiaId ID de la estadía
	 * @return Estadia encontrada
	 * @throws NoSuchElementException si no existe la estadía con ese ID
	 */
	public Estadia obtenerEstadiaPorId(Long estadiaId) {
		return estadiaRepository.findById(estadiaId)
				.orElseThrow(() -> new NoSuchElementException(
						"No se encontró la estadía con ID: " + estadiaId));
	}
}
