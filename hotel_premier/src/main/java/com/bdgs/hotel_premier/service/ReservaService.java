package com.bdgs.hotel_premier.service;
import com.bdgs.hotel_premier.dto.ConflictoReservaDto;
import com.bdgs.hotel_premier.dto.ReservaCancelarDto;
import com.bdgs.hotel_premier.dto.ReservaDto;
import com.bdgs.hotel_premier.dto.ReservaFiltroDto;
import com.bdgs.hotel_premier.dto.ReservaInfoDto;
import com.bdgs.hotel_premier.entity.Reserva;
import com.bdgs.hotel_premier.repository.ReservaRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import com.bdgs.hotel_premier.enums.EstadoReserva;

/**
 * Servicio para la gestión de reservas en el hotel.
 * 
 * <p>Proporciona operaciones para crear, cancelar, consultar y filtrar reservas.
 * Gestiona la persistencia y consultas básicas, mientras que la orquestación
 * compleja se delega a {@link ReservaAltaFacade}.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // El servicio se limita a persistencia y consultas; la orquestación vive en ReservaAltaFacade.

    public Reserva agregarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    /**
     * Actualiza una reserva existente (por ejemplo, cambio de estado).
     * No crea una nueva reserva, solo persiste los cambios en la entidad.
     */
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

	/**
	 * Obtiene las reservas que se superponen con el rango [inicio, finExclusivo).
	 */
	public List<Reserva> obtenerReservasPorRango(LocalDate inicio, LocalDate finExclusivo) {
		return reservaRepository.findReservasPorRango(inicio, finExclusivo);
	}

	public List<ReservaInfoDto> buscarReservas(ReservaFiltroDto filtro) {
		Reserva ejemplo = Reserva.builder()
				.nombre(filtro.getNombre())
				.apellido(filtro.getApellido())
				.build();
		
		if(ejemplo.getNombre() != null && ejemplo.getNombre().trim().isBlank()) {
			ejemplo.setNombre(null);
		}

		ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues() // ignora filtros no enviados (ahora incluye strings vacíos normalizados)
                .withIgnoreCase(); // insensible a mayúsculas/minúsculas
        
		Example<Reserva> example = Example.of(ejemplo, matcher);
		List<Reserva> reservas = reservaRepository.findAll(example);
		
		List<ReservaInfoDto> resultado = new ArrayList<>();

		for (Reserva reserva : reservas) {

			if(reserva.getEstado() == EstadoReserva.CANCELADA) {
				continue; // Saltar reservas canceladas
			}

			ReservaInfoDto dto = ReservaInfoDto.builder()
					.nombre(reserva.getNombre())
					.apellido(reserva.getApellido())
					.fechaInicio(reserva.getFechaInicio())
					.fechaFin(reserva.getFechaFin())
					.idHabitacion(reserva.getHabitacion().getId())
					.tipoHabitacion(reserva.getHabitacion().getTipoHabitacion().getDescripcion())
					.build();
			
			resultado.add(dto);
		}

		return resultado;
	}

	/**
	 * Devuelve, por habitación, el conjunto de días reservados dentro de [inicio, finExclusivo).
	 * Considera reservas CONFIRMADA y NO_CONFIRMADA. Fin exclusivo para evitar contar checkout.
	 */
	public Map<Long, Set<LocalDate>> obtenerDiasReservadosPorHabitacion(LocalDate inicio, LocalDate finExclusivo) {
		List<Reserva> reservas = obtenerReservasPorRango(inicio, finExclusivo);
		Map<Long, Set<LocalDate>> mapa = new HashMap<>();

		for (Reserva r : reservas) {
			if (r.getEstado() != EstadoReserva.CONFIRMADA && r.getEstado() != EstadoReserva.NO_CONFIRMADA) {
				continue;
			}
			Long habId = r.getHabitacion().getId();
			Set<LocalDate> dias = mapa.computeIfAbsent(habId, k -> new HashSet<>());
			agregarDiasSolapados(dias, r.getFechaInicio(), r.getFechaFin(), inicio, finExclusivo);
		}

		return mapa;
	}

	/**
	 * Obtiene los conflictos de reservas para una habitación específica en un rango.
	 * Útil para mostrar "quién tiene la reserva" antes de decidir ocupar sobre reserva.
	 */
	public List<ConflictoReservaDto> obtenerConflictosReservas(Long habitacionId, LocalDate inicio, LocalDate fin) {
		LocalDate finExclusivo = fin.plusDays(1);
		List<Reserva> reservas = obtenerReservasPorRango(inicio, finExclusivo);
		List<ConflictoReservaDto> conflictos = new ArrayList<>();

		for (Reserva r : reservas) {
			if (!r.getHabitacion().getId().equals(habitacionId)) continue;
			if (r.getEstado() != EstadoReserva.CONFIRMADA && r.getEstado() != EstadoReserva.NO_CONFIRMADA) continue;

			// Calcular días de conflicto dentro del rango solicitado
			Set<LocalDate> diasConflicto = new HashSet<>();
			agregarDiasSolapados(diasConflicto, r.getFechaInicio(), r.getFechaFin(), inicio, finExclusivo);

			conflictos.add(ConflictoReservaDto.builder()
					.reservaId(r.getId())
					.titular(r.getApellido() + ", " + r.getNombre())
					.telefono(r.getTelefono())
					.diasConflicto(diasConflicto)
					.build());
		}

		return conflictos;
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

	/**
	 * Cancela múltiples reservas cambiando su estado a CANCELADA.
	 * Identifica las reservas por la combinación exacta de todos los datos.
	 * 
	 * @param reservas lista de DTOs con los datos de las reservas a cancelar
	 * @return lista de DTOs con las reservas canceladas
	 */
	public List<ReservaDto> cancelarReservas(List<ReservaCancelarDto> reservas) {
		List<ReservaDto> resultado = new ArrayList<>();
		
		for (ReservaCancelarDto dto : reservas) {
			Reserva reserva = reservaRepository.findByNombreAndApellidoAndFechaInicioAndFechaFinAndHabitacionId(
				dto.getNombre(), 
				dto.getApellido(), 
				dto.getFechaInicio(), 
				dto.getFechaFin(),
				dto.getIdHabitacion()
			);
			
			if (reserva != null) {
				reserva.setEstado(EstadoReserva.CANCELADA);
				Reserva guardada = reservaRepository.save(reserva);
				
				resultado.add(ReservaDto.builder()
						.nombre(guardada.getNombre())
						.apellido(guardada.getApellido())
						.telefono(guardada.getTelefono())
						.fechaInicio(guardada.getFechaInicio())
						.fechaFin(guardada.getFechaFin())
						.idHabitacion(guardada.getHabitacion().getId())
						.build());
			}
		}
		
		return resultado;
	}
}
