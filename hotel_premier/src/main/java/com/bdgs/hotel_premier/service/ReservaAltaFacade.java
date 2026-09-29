package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.ReservaDto;
import com.bdgs.hotel_premier.dto.ReservaSolicitudDto;
import com.bdgs.hotel_premier.dto.ReservaSolicitudItemDto;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.entity.Reserva;
import com.bdgs.hotel_premier.enums.EstadoReserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Facade para el caso de uso de Alta de Reservas (CU04 Reservar Habitación).
 * <p>
 * Orquesta validaciones de disponibilidad de habitaciones y delega la persistencia
 * de reservas en {@link ReservaService}.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class ReservaAltaFacade {

    private final HabitacionService habitacionService;
    private final ReservaService reservaService;
    private final DisponibilidadHabitacionFacade disponibilidadFacade;

    public ReservaAltaFacade(HabitacionService habitacionService, ReservaService reservaService, DisponibilidadHabitacionFacade disponibilidadFacade) {
        this.habitacionService = habitacionService;
        this.reservaService = reservaService;
        this.disponibilidadFacade = disponibilidadFacade;
    }

    @Transactional
    public List<ReservaDto> reservar(ReservaSolicitudDto solicitud) {
        List<ReservaDto> respuesta = new ArrayList<>();
        for (ReservaSolicitudItemDto item : solicitud.getItems()) {
            Habitacion hab = habitacionService.obtenerHabitacionPorId(item.getHabitacionId());
            if (hab == null) {
                throw new IllegalArgumentException("Habitación no encontrada: " + item.getHabitacionId());
            }
            // Validar disponibilidad del rango solicitado
            if (!disponibilidadFacade.rangoDisponible(hab.getId(), item.getFechaInicio(), item.getFechaFin())) {
                throw new IllegalStateException("La habitación " + hab.getId() + " no está disponible entre " + item.getFechaInicio() + " y " + item.getFechaFin());
            }
            // Crear reserva NO_CONFIRMADA
            Reserva reserva = Reserva.builder()
                    .nombre(solicitud.getNombre())
                    .apellido(solicitud.getApellido())
                    .telefono(solicitud.getTelefono())
                    .fechaInicio(item.getFechaInicio())
                    .fechaFin(item.getFechaFin())
                    .habitacion(hab)
                    .estado(EstadoReserva.NO_CONFIRMADA)
                    .build();
            Reserva guardada = reservaService.agregarReserva(reserva);
            respuesta.add(ReservaDto.builder()
                    .nombre(guardada.getNombre())
                    .apellido(guardada.getApellido())
                    .telefono(guardada.getTelefono())
                    .fechaInicio(guardada.getFechaInicio())
                    .fechaFin(guardada.getFechaFin())
                    .idHabitacion(guardada.getHabitacion().getId())
                    .build());
        }
        return respuesta;
    }
}
