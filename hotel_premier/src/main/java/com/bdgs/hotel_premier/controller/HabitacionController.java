package com.bdgs.hotel_premier.controller;

import com.bdgs.hotel_premier.dto.ConflictoReservaDto;
import com.bdgs.hotel_premier.dto.EstadoHabitacionDto;
import com.bdgs.hotel_premier.service.DisponibilidadHabitacionFacade;
import com.bdgs.hotel_premier.service.ReservaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar consultas de disponibilidad de habitaciones.
 * 
 * <p>Proporciona endpoints para verificar disponibilidad de habitaciones
 * en rangos de fechas específicos y detectar conflictos con reservas o estadías.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@RestController
public class HabitacionController {
    private final DisponibilidadHabitacionFacade disponibilidadFacade;
    private final ReservaService reservaService;

    public HabitacionController(DisponibilidadHabitacionFacade disponibilidadFacade, ReservaService reservaService) {
        this.disponibilidadFacade = disponibilidadFacade;
        this.reservaService = reservaService;
    }

    /**
     * Endpoint de solo consulta que devuelve la grilla de disponibilidad
     * por día y habitación en el rango [inicio, fin]. No crea ni modifica estados.
     */
    @GetMapping("/habitaciones/disponibilidad")
    public ResponseEntity<List<EstadoHabitacionDto>> consultarDisponibilidad(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<EstadoHabitacionDto> disponibilidad = disponibilidadFacade.mostrarDisponibilidad(inicio, fin);
        return ResponseEntity.ok(disponibilidad);
    }

    /**
     * Endpoint para consultar conflictos con reservas en una habitación específica.
     * Útil para mostrar "quién tiene la reserva" antes de decidir "OCUPAR IGUAL".
     */
    @GetMapping("/habitaciones/conflictos-reservas")
    public ResponseEntity<List<ConflictoReservaDto>> consultarConflictosReservas(
            @RequestParam("habitacionId") Long habitacionId,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        List<ConflictoReservaDto> conflictos = reservaService.obtenerConflictosReservas(habitacionId, inicio, fin);
        return ResponseEntity.ok(conflictos);
    }
}
