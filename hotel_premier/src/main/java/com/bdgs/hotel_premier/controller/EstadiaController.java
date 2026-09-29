package com.bdgs.hotel_premier.controller;
import com.bdgs.hotel_premier.dto.EstadiaResultadoDto;
import com.bdgs.hotel_premier.dto.EstadiaSolicitudDto;
import com.bdgs.hotel_premier.service.EstadiaAltaFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones de estadías.
 * 
 * <p>Proporciona endpoints para ocupar habitaciones y crear estadías
 * asociadas a reservas existentes (CU15 - Ocupar Habitación).</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@RestController
public class EstadiaController {

    private final EstadiaAltaFacade estadiaAltaFacade;

    public EstadiaController(EstadiaAltaFacade estadiaAltaFacade) {
        this.estadiaAltaFacade = estadiaAltaFacade;
    }

    @PostMapping("/estadias")
    public ResponseEntity<List<EstadiaResultadoDto>> ocupar(@RequestBody EstadiaSolicitudDto solicitud) {
        List<EstadiaResultadoDto> resultado = estadiaAltaFacade.ocupar(solicitud);
        return ResponseEntity.status(201).body(resultado);
    }
}
