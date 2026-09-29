package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa un ítem individual dentro de una solicitud de estadía.
 * <p>
 * Define una habitación específica con su rango de fechas y opciones de ocupación,
 * permitiendo ocupar sobre reservas existentes si se especifica.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadiaSolicitudItemDto {
    private Long habitacionId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean ocuparSobreReserva; // permite ocupar días con estado RESERVADA
    private Long reservaIdAsociada; // opcional, si el check-in corresponde a una reserva propia
}