package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar conflictos de reservas en una habitación.
 * <p>
 * Utilizado para informar sobre reservas existentes que se superponen
 * con nuevas solicitudes de reserva o estadía.
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
public class ConflictoReservaDto {
    private Long reservaId;
    private String titular; // apellido, nombre
    private String telefono;
    private Set<LocalDate> diasConflicto;
}