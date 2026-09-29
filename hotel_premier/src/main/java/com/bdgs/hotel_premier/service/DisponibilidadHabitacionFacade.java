package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.EstadoHabitacionDto;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.enums.EstadoHabitacion;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.Set;

/**
 * Facade que centraliza la lógica de disponibilidad de habitaciones.
 * <p>
 * Elimina dependencias entre servicios para consultas de disponibilidad,
 * consolidando verificaciones de reservas, estadías y estados de habitaciones.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class DisponibilidadHabitacionFacade {

    private final HabitacionService habitacionService;
    private final ReservaService reservaService;
    private final EstadiaService estadiaService;

    public DisponibilidadHabitacionFacade(HabitacionService habitacionService,
                                          ReservaService reservaService,
                                          EstadiaService estadiaService) {
        this.habitacionService = habitacionService;
        this.reservaService = reservaService;
        this.estadiaService = estadiaService;
    }

    /**
     * Devuelve una lista plana con el estado de cada habitación por día
     * dentro del rango [fechaInicio, fechaFin]. Esta lista permite al front
     * construir una grilla Día x Habitación.
     */
    public List<EstadoHabitacionDto> mostrarDisponibilidad(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }

        // Obtener habitaciones del servicio correspondiente
        // Necesitamos agregar este método a HabitacionService
        LocalDate finExclusivo = fechaFin.plusDays(1);

        // Obtener datos usando solo servicios
        Map<Long, Set<LocalDate>> diasReservadosPorHab = reservaService.obtenerDiasReservadosPorHabitacion(fechaInicio, finExclusivo);
        Map<Long, Set<LocalDate>> diasOcupadosPorHab = estadiaService.obtenerDiasOcupadosPorHabitacion(fechaInicio, finExclusivo);
        Map<Long, Set<LocalDate>> diasNoDisponiblesPorHab = habitacionService.obtenerDiasNoDisponiblesPorHabitacion(fechaInicio, finExclusivo);
        List<Habitacion> habitaciones = habitacionService.obtenerTodasLasHabitaciones();

        List<EstadoHabitacionDto> resultado = new ArrayList<>();

        for (LocalDate d = fechaInicio; !d.isAfter(fechaFin); d = d.plusDays(1)) {
            for (Habitacion hab : habitaciones) {
                EstadoHabitacion estado = determinarEstadoHabitacion(
                    d, hab.getId(), 
                    diasNoDisponiblesPorHab, 
                    diasOcupadosPorHab, 
                    diasReservadosPorHab
                );

                resultado.add(EstadoHabitacionDto.builder()
                        .dia(d)
                        .idHabitacion(hab.getId())
                        .estadoHabitacion(estado)
                        .build());
            }
        }

        return resultado;
    }

    /**
     * Verifica si todo el rango [inicio, fin] (inclusive) está disponible para la habitación indicada.
     * Usa la misma lógica de prioridad que la grilla.
     */
    public boolean rangoDisponible(Long habitacionId, LocalDate inicio, LocalDate fin) {
        if (habitacionId == null || inicio == null || fin == null || inicio.isAfter(fin)) {
            throw new IllegalArgumentException("Parámetros inválidos para verificación de disponibilidad");
        }

        LocalDate finExclusivo = fin.plusDays(1);

        // Obtener datos: servicios para reservas/estadias, HabitacionService para no disponibilidades
        Map<Long, Set<LocalDate>> diasReservadosPorHab = reservaService.obtenerDiasReservadosPorHabitacion(inicio, finExclusivo);
        Map<Long, Set<LocalDate>> diasOcupadosPorHab = estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, finExclusivo);
        Set<LocalDate> diasNoDisponibles = habitacionService.obtenerDiasNoDisponibles(habitacionId, inicio, finExclusivo);

        Set<LocalDate> diasReservados = diasReservadosPorHab.get(habitacionId);
        Set<LocalDate> diasOcupados = diasOcupadosPorHab.get(habitacionId);

        for (LocalDate d = inicio; !d.isAfter(fin); d = d.plusDays(1)) {
            if (estaEnSet(d, diasNoDisponibles) || estaEnSet(d, diasOcupados) || estaEnSet(d, diasReservados)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si el rango está disponible permitiendo ocupar sobre reservas.
     * Solo verifica NO_DISPONIBLE y OCUPADA, ignorando RESERVADA.
     */
    public boolean rangoDisponiblePermitirSobreReserva(Long habitacionId, LocalDate inicio, LocalDate fin) {
        if (habitacionId == null || inicio == null || fin == null || inicio.isAfter(fin)) {
            throw new IllegalArgumentException("Parámetros inválidos para verificación de disponibilidad");
        }

        LocalDate finExclusivo = fin.plusDays(1);

        Map<Long, Set<LocalDate>> diasOcupadosPorHab = estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, finExclusivo);
        Set<LocalDate> diasNoDisponibles = habitacionService.obtenerDiasNoDisponibles(habitacionId, inicio, finExclusivo);
        Set<LocalDate> diasOcupados = diasOcupadosPorHab.get(habitacionId);

        for (LocalDate d = inicio; !d.isAfter(fin); d = d.plusDays(1)) {
            if (estaEnSet(d, diasNoDisponibles) || estaEnSet(d, diasOcupados)) {
                return false;
            }
        }
        return true;
    }





    /**
     * Determina el estado de una habitación en un día específico aplicando prioridades.
     */
    private EstadoHabitacion determinarEstadoHabitacion(LocalDate dia, Long habitacionId,
                                                        Map<Long, Set<LocalDate>> diasNoDisponiblesPorHab,
                                                        Map<Long, Set<LocalDate>> diasOcupadosPorHab,
                                                        Map<Long, Set<LocalDate>> diasReservadosPorHab) {
        // Prioridad 1: NO_DISPONIBLE
        if (estaEnSet(dia, diasNoDisponiblesPorHab.get(habitacionId))) {
            return EstadoHabitacion.NO_DISPONIBLE;
        }
        // Prioridad 2: OCUPADA
        if (estaEnSet(dia, diasOcupadosPorHab.get(habitacionId))) {
            return EstadoHabitacion.OCUPADA;
        }
        // Prioridad 3: RESERVADA
        if (estaEnSet(dia, diasReservadosPorHab.get(habitacionId))) {
            return EstadoHabitacion.RESERVADA;
        }
        // Prioridad 4: DISPONIBLE
        return EstadoHabitacion.DISPONIBLE;
    }



    private boolean estaEnSet(LocalDate dia, Set<LocalDate> dias) {
        return dias != null && dias.contains(dia);
    }
}