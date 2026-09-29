package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import com.bdgs.hotel_premier.enums.TipoDocumento;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO simplificado para representar datos básicos de un huésped.
 * <p>
 * Contiene solo los campos esenciales para identificación y visualización,
 * sin incluir validaciones ni campos opcionales.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class HuespedDataDto {
    private final String apellido;
    private final String nombre;
    private final TipoDocumento tipoDocumento;
    private final String numeroDocumento;
    private final LocalDate fechaNacimiento;
}
