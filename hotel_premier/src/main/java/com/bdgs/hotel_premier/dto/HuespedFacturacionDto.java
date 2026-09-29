package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import com.bdgs.hotel_premier.enums.PosicionIva;
import com.bdgs.hotel_premier.enums.TipoDocumento;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO que representa los datos de un huésped en la grilla de selección
 * de responsable de pago durante el proceso de facturación.
 * 
 * <p>Incluye información básica del huésped más campos necesarios para
 * determinar si puede ser responsable de pago (edad, posición IVA).</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class HuespedFacturacionDto {
    private final Long id;
    private final String apellido;
    private final String nombre;
    private final TipoDocumento tipoDocumento;
    private final String numeroDocumento;
    private final LocalDate fechaNacimiento;
    private final Integer edad;
    private final PosicionIva posicionIva;
    private final String cuit;
}
