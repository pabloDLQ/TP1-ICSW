package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO que representa un ítem individual dentro de una solicitud de reserva.
 * <p>
 * Cada ítem corresponde a una habitación específica con su rango de fechas.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Builder
public class ReservaSolicitudItemDto {
    @NotNull
    private final Long habitacionId;
    @NotNull
    private final LocalDate fechaInicio;
    @NotNull
    private final LocalDate fechaFin;
}
