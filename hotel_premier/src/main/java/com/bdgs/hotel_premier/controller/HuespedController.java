package com.bdgs.hotel_premier.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bdgs.hotel_premier.dto.HuespedDto;
import com.bdgs.hotel_premier.dto.HuespedEliminarDto;
import com.bdgs.hotel_premier.dto.HuespedFiltroDto;
import com.bdgs.hotel_premier.service.HuespedService;
import com.bdgs.hotel_premier.enums.TipoDocumento;

import jakarta.validation.Valid;


/**
 * Controlador REST para gestionar operaciones relacionadas con huéspedes.
 * 
 * <p>Proporciona endpoints para dar de alta, consultar, modificar y eliminar
 * huéspedes del sistema hotelero.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@RestController
public class HuespedController {
    private final HuespedService huespedService;

    public HuespedController(HuespedService huespedService) {
        this.huespedService = huespedService;
    }

    @GetMapping("/huespedes")
    public ResponseEntity<List<HuespedDto>> buscarHuesped(HuespedFiltroDto huespedFiltroDto) {
        List<HuespedDto> listaHuespedesDto = huespedService.buscarHuespedes(huespedFiltroDto);
        return ResponseEntity.ok(listaHuespedesDto);
    }
    
    @PostMapping("/huespedes")
    public ResponseEntity<HuespedDto> darAltaHuesped(@Valid @RequestBody HuespedDto huespedDto) {
        HuespedDto huesped = huespedService.agregarHuesped(huespedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(huesped);
    }

    //NOTA: Modificar huesped no esta implementado en el frontend y no se usa en el backend actualmente.
    //Por lo que no nos detuvimos a controlar que funcione correctamente.
    @PutMapping("/huespedes")
    public ResponseEntity<HuespedDto> modificarHuesped(
        @Valid @RequestBody HuespedDto huespedDto,
        TipoDocumento originalTipo,
        String originalNumero
    ) {
        // Si no se proveen originales, se asume que no cambia el identificador
        HuespedDto resultado;
        if (originalTipo == null || originalNumero == null) {
            resultado = huespedService.modificarHuesped(huespedDto);
        } else {
            resultado = huespedService.modificarHuesped(originalTipo, originalNumero, huespedDto);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/huespedes")
    public ResponseEntity<HuespedDto> eliminarHuesped(@Valid @RequestBody HuespedEliminarDto huespedDto) {
        HuespedDto resultado = huespedService.eliminarHuesped(huespedDto);
        return ResponseEntity.ok(resultado);
    }
}
