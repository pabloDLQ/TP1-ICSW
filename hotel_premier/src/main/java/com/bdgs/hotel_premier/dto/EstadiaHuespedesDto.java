package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO que representa la respuesta del endpoint que obtiene los huéspedes de una estadía para facturación.
 * <p>
 * Contiene la lista de huéspedes de la estadía y datos básicos de la misma
 * necesarios para mostrar contexto al usuario (Paso 5 - Grilla de huéspedes).
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class EstadiaHuespedesDto {
    private final Long estadiaId;
    private final Long habitacionId;
    private final String codigoHabitacion;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final List<HuespedFacturacionDto> huespedes;
}
