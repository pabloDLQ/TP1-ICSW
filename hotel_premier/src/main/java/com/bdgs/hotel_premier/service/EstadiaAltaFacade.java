package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.ConflictoReservaDto;
import com.bdgs.hotel_premier.dto.EstadiaResultadoDto;
import com.bdgs.hotel_premier.dto.EstadiaSolicitudDto;
import com.bdgs.hotel_premier.dto.EstadiaSolicitudItemDto;
import com.bdgs.hotel_premier.dto.HuespedRefDto;
import com.bdgs.hotel_premier.entity.Estadia;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.entity.Huesped;
import com.bdgs.hotel_premier.entity.Reserva;
import com.bdgs.hotel_premier.enums.EstadoReserva;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Facade para orquestar el proceso completo de alta de estadías.
 * 
 * <p>Coordina la validación de reservas, verificación de huéspedes,
 * asignación de habitaciones y creación de estadías, integrando
 * {@link ReservaService}, {@link HuespedService}, {@link HabitacionService}
 * y {@link EstadiaService}.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class EstadiaAltaFacade {

    private final HabitacionService habitacionService;
    private final EstadiaService estadiaService;
    private final ReservaService reservaService;
    private final HuespedService huespedService;
    private final DisponibilidadHabitacionFacade disponibilidadFacade;

    public EstadiaAltaFacade(HabitacionService habitacionService,
                             EstadiaService estadiaService,
                             ReservaService reservaService,
                             HuespedService huespedService,
                             DisponibilidadHabitacionFacade disponibilidadFacade) {
        this.habitacionService = habitacionService;
        this.estadiaService = estadiaService;
        this.reservaService = reservaService;
        this.huespedService = huespedService;
        this.disponibilidadFacade = disponibilidadFacade;
    }

    @Transactional
    public List<EstadiaResultadoDto> ocupar(EstadiaSolicitudDto solicitud) {
        // Validar existencia de huesped principal y acompañantes
        Huesped principal = obtenerHuesped(solicitud.getHuespedPrincipal());
        List<Huesped> acompanantes = new ArrayList<>();
        if (solicitud.getAcompanantes() != null) {
            for (HuespedRefDto ref : solicitud.getAcompanantes()) {
                acompanantes.add(obtenerHuesped(ref));
            }
        }

        Set<Huesped> conjuntoHuespedes = new HashSet<>();
        conjuntoHuespedes.add(principal);
        conjuntoHuespedes.addAll(acompanantes);
        
        // Validar que hay al menos un huésped
        if (conjuntoHuespedes.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un huésped para la estadia");
        }

        List<EstadiaResultadoDto> respuesta = new ArrayList<>();

        for (EstadiaSolicitudItemDto item : solicitud.getItems()) {
            if (item.getFechaInicio() == null || item.getFechaFin() == null || item.getFechaInicio().isAfter(item.getFechaFin())) {
                throw new IllegalArgumentException("Rango de fechas inválido para ocupación");
            }

            Habitacion hab = habitacionService.obtenerHabitacionPorId(item.getHabitacionId());
            if (hab == null) {
                throw new IllegalArgumentException("Habitación no encontrada: " + item.getHabitacionId());
            }

            // Validar ocupabilidad del rango
            validarRangoOcupable(hab.getId(), item.getFechaInicio(), item.getFechaFin(), item.isOcuparSobreReserva());

            // Determinar si asociar a una reserva específica
            Reserva reserva = null;
            if (item.getReservaIdAsociada() != null) {
                // Caso 1: Se especifica una reserva concreta para asociar
                reserva = obtenerYValidarReserva(item.getReservaIdAsociada(), hab.getId(), item.getFechaInicio(), item.getFechaFin());
            } else if (item.isOcuparSobreReserva()) {
                // Caso 2: Se permite ocupar sobre reserva, encontrar automáticamente una reserva que se superponga
                reserva = buscarReservaSuperpuesta(hab.getId(), item.getFechaInicio(), item.getFechaFin());
            }
            // Caso 3: Ocupación normal sin reserva asociada (reserva queda null)

            // Crear y persistir una sola Estadia
            Estadia estadia = Estadia.builder()
                    .habitacion(hab)
                    .fechaInicio(item.getFechaInicio())
                    .fechaFin(item.getFechaFin())
                    .huespedes(conjuntoHuespedes)
                    .reserva(reserva)
                    .build();
            Estadia guardada = estadiaService.guardarEstadia(estadia);
            
            // Si hay una reserva asociada, cambiar su estado a CONFIRMADA
            if (reserva != null) {
                reserva.setEstado(EstadoReserva.CONFIRMADA);
                reservaService.guardarReserva(reserva);
            }

            respuesta.add(EstadiaResultadoDto.builder()
                    .idEstadia(guardada.getId())
                    .habitacionId(hab.getId())
                    .fechaInicio(guardada.getFechaInicio())
                    .fechaFin(guardada.getFechaFin())
                    .huespedPrincipal(solicitud.getHuespedPrincipal())
                    .acompanantes(solicitud.getAcompanantes())
                    .build());
        }

        return respuesta;
    }

    private Huesped obtenerHuesped(HuespedRefDto ref) {
        if (ref == null || ref.getTipoDocumento() == null || ref.getNumeroDocumento() == null) {
            throw new IllegalArgumentException("Referencia de huésped inválida");
        }
        return huespedService.obtenerHuespedPorDocumento(ref.getTipoDocumento(), ref.getNumeroDocumento())
                .orElseThrow(() -> new IllegalArgumentException("Huésped no existe: " + ref.getTipoDocumento() + " " + ref.getNumeroDocumento()));
    }

    /**
     * Valida que el rango sea ocupable: no permite NO_DISPONIBLE ni OCUPADA.
     * Si permitirSobreReserva=false, tampoco permite días RESERVADA.
     */
    private void validarRangoOcupable(Long habitacionId, LocalDate inicio, LocalDate fin, boolean permitirSobreReserva) {
        if (!permitirSobreReserva) {
            // Si no permite sobre reserva, verificar disponibilidad completa incluyendo reservas
            if (!disponibilidadFacade.rangoDisponible(habitacionId, inicio, fin)) {
                throw new IllegalStateException("La habitación " + habitacionId + " no está disponible en el rango solicitado");
            }
        } else {
            // Si permite sobre reserva, verificar solo NO_DISPONIBLE y OCUPADA
            if (!disponibilidadFacade.rangoDisponiblePermitirSobreReserva(habitacionId, inicio, fin)) {
                throw new IllegalStateException("La habitación " + habitacionId + " no está disponible para ocupación (bloqueada u ocupada)");
            }
        }
    }
    
    /**
     * Busca automáticamente una reserva que se superponga con el rango de la estadia.
     * Devuelve la primera reserva encontrada o null si no hay ninguna.
     */
    private Reserva buscarReservaSuperpuesta(Long habitacionId, LocalDate inicio, LocalDate fin) {
        List<ConflictoReservaDto> conflictos = reservaService.obtenerConflictosReservas(habitacionId, inicio, fin);
        
        if (!conflictos.isEmpty()) {
            // Tomar la primera reserva que se superpone
            Long reservaId = conflictos.get(0).getReservaId();
            return reservaService.obtenerReservaPorId(reservaId);
        }
        
        return null; // No hay reservas superpuestas
    }
    
    /**
     * Obtiene y valida una reserva específica para asociar a una estadia.
     */
    private Reserva obtenerYValidarReserva(Long reservaId, Long habitacionId, LocalDate inicio, LocalDate fin) {
        Reserva reserva = reservaService.obtenerReservaPorId(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva asociada no encontrada: " + reservaId);
        }
        if (!reserva.getHabitacion().getId().equals(habitacionId)) {
            throw new IllegalStateException("La reserva asociada pertenece a otra habitación");
        }
        
        // Validar solapamiento con rango
        LocalDate finEx = fin.plusDays(1);
        boolean solapa = reserva.getFechaInicio().isBefore(finEx) && 
                        reserva.getFechaFin().isAfter(inicio);
        if (!solapa) {
            throw new IllegalStateException("La reserva asociada no solapa el rango de ocupación");
        }
        
        return reserva;
    }
}