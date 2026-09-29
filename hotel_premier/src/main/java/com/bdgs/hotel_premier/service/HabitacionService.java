package com.bdgs.hotel_premier.service;
import com.bdgs.hotel_premier.dto.ReservaDto;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.entity.NoDisponibilidadHabitacion;
import com.bdgs.hotel_premier.repository.HabitacionRepository;
import com.bdgs.hotel_premier.repository.NoDisponibilidadHabitacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Servicio para la gestión de habitaciones y su disponibilidad.
 * 
 * <p>Proporciona operaciones para consultar habitaciones, verificar disponibilidad,
 * listar todas las habitaciones y gestionar períodos de no disponibilidad.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class HabitacionService {
    private final HabitacionRepository habitacionRepository;
    private final NoDisponibilidadHabitacionRepository noDisponibilidadRepo;

    public HabitacionService(HabitacionRepository habitacionRepository,
                             NoDisponibilidadHabitacionRepository noDisponibilidadRepo) {
        this.habitacionRepository = habitacionRepository;
        this.noDisponibilidadRepo = noDisponibilidadRepo;
    }

    public Habitacion obtenerHabitacionPorId(Long id) {
        return habitacionRepository.findById(id).orElse(null);
    }



    public List<Habitacion> obtenerHabitacionesParaReservas(List<ReservaDto> reservaDtos) {
        List<Habitacion> habitaciones = new ArrayList<>();
        for(ReservaDto dto : reservaDtos) {
            Habitacion hab = obtenerHabitacionPorId(dto.getIdHabitacion());
            if (hab == null) {
                throw new IllegalArgumentException("Habitación no encontrada: " + dto.getIdHabitacion());
            }
            habitaciones.add(hab);
        }
        return habitaciones;
    }

    /**
     * Obtiene todas las habitaciones del sistema.
     * Método necesario para el Facade de disponibilidad.
     */
    public List<Habitacion> obtenerTodasLasHabitaciones() {
        return habitacionRepository.findAll();
    }



    /**
     * Obtiene los días no disponibles para una habitación específica en un rango de fechas.
     * HabitacionService maneja directamente NoDisponibilidadHabitacion (su dominio).
     */
    public Set<LocalDate> obtenerDiasNoDisponibles(Long habitacionId, LocalDate inicio, LocalDate finExclusivo) {
        List<NoDisponibilidadHabitacion> indis = noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicio, finExclusivo);
        Set<LocalDate> diasNoDisponibles = new HashSet<>();
        
        for (NoDisponibilidadHabitacion n : indis) {
            if (!n.getHabitacion().getId().equals(habitacionId)) continue;
            agregarDiasSolapados(diasNoDisponibles, n.getFechaInicio(), n.getFechaFin(), inicio, finExclusivo);
        }
        
        return diasNoDisponibles;
    }

    /**
     * Obtiene un mapa con los días no disponibles por habitación en un rango de fechas.
     * HabitacionService maneja directamente NoDisponibilidadHabitacion (su dominio).
     */
    public Map<Long, Set<LocalDate>> obtenerDiasNoDisponiblesPorHabitacion(LocalDate inicio, LocalDate finExclusivo) {
        List<NoDisponibilidadHabitacion> indis = noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicio, finExclusivo);
        Map<Long, Set<LocalDate>> diasNoDisponiblesPorHab = new HashMap<>();
        
        for (NoDisponibilidadHabitacion n : indis) {
            Long habId = n.getHabitacion().getId();
            Set<LocalDate> dias = diasNoDisponiblesPorHab.computeIfAbsent(habId, k -> new HashSet<>());
            agregarDiasSolapados(dias, n.getFechaInicio(), n.getFechaFin(), inicio, finExclusivo);
        }
        
        return diasNoDisponiblesPorHab;
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
}
