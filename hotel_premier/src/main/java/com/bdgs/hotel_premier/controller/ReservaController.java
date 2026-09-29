package com.bdgs.hotel_premier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bdgs.hotel_premier.dto.ReservaCancelarDto;
import com.bdgs.hotel_premier.dto.ReservaDto;
import com.bdgs.hotel_premier.dto.ReservaFiltroDto;
import com.bdgs.hotel_premier.dto.ReservaInfoDto;
import com.bdgs.hotel_premier.dto.ReservaSolicitudDto;
import com.bdgs.hotel_premier.service.ReservaAltaFacade;
import com.bdgs.hotel_premier.service.ReservaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;


/**
 * Controlador REST para gestionar operaciones relacionadas con reservas.
 * 
 * <p>Proporciona endpoints para crear reservas, consultar disponibilidad,
 * filtrar reservas existentes y cancelar reservas (CU04 - Reservar Habitación).</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@RestController
public class ReservaController {

    private final ReservaAltaFacade reservaAltaFacade;
    private final ReservaService reservaService;

    public ReservaController(ReservaAltaFacade reservaAltaFacade, ReservaService reservaService) {
        this.reservaAltaFacade = reservaAltaFacade;
        this.reservaService = reservaService;
    }

    @PostMapping("/reservas")
    public ResponseEntity<List<ReservaDto>> reservar(@Valid @RequestBody ReservaSolicitudDto solicitud) {
        List<ReservaDto> resultado = reservaAltaFacade.reservar(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaInfoDto>> obtenerReservas(@Valid ReservaFiltroDto filtro) {
        List<ReservaInfoDto> resultado = reservaService.buscarReservas(filtro);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    
    @PatchMapping("/reservas/cancelar")
    public ResponseEntity<List<ReservaDto>> cancelarReservas(@RequestBody List<ReservaCancelarDto> reservas) {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.cancelarReservas(reservas));
    }
}
