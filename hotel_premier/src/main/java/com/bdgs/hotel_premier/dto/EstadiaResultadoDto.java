package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar el resultado de la creación de una estadía.
 * <p>
 * Contiene el ID generado, información de la habitación, fechas,
 * huésped principal y acompañantes de la estadía creada.
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
public class EstadiaResultadoDto {
    private Long idEstadia;
    private Long habitacionId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private HuespedRefDto huespedPrincipal;
    private List<HuespedRefDto> acompanantes;
}