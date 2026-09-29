package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para solicitudes de cancelación de reservas.
 * <p>
 * Identifica la reserva a cancelar mediante los datos del titular,
 * fechas y habitación asociada.
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
public class ReservaCancelarDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long idHabitacion;
}
