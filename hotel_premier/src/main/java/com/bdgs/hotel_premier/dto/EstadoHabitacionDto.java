package com.bdgs.hotel_premier.dto;

import com.bdgs.hotel_premier.enums.EstadoHabitacion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * DTO para representar el estado de una habitación en un día específico.
 * <p>
 * Utilizado para consultar y mostrar la disponibilidad diaria de habitaciones.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class EstadoHabitacionDto {
    private final LocalDate dia;
    private final Long idHabitacion;
    private final EstadoHabitacion estadoHabitacion;
}
