package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para consultar información de una estadía activa.
 * <p>
 * Requiere el ID de la habitación y la fecha de fin para identificar la estadía.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class EstadiaConsultaDto {
    @NotNull(message = "El ID de la habitación es obligatorio")
    private final Long idHabitacion;

    @NotNull(message = "La fecha de fin es obligatoria")
    private final LocalDate fechaFin;
}
