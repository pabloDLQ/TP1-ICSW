package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar información de una reserva simple.
 * <p>
 * Incluye datos del titular, fechas de reserva y la habitación asociada.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class ReservaDto {
    private final String nombre;
    private final String apellido;
    private final String telefono;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final Long idHabitacion;
}
