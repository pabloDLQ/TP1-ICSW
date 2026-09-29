package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO para mostrar información detallada de una reserva.
 * <p>
 * Incluye datos del titular, fechas y detalles de la habitación incluyendo su tipo.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class ReservaInfoDto {
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final Long idHabitacion;
    private final String tipoHabitacion;
}
