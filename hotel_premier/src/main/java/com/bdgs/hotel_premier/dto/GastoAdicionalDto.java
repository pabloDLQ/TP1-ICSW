package com.bdgs.hotel_premier.dto;

import com.bdgs.hotel_premier.enums.TipoGastoAdicional;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar un gasto adicional de una estadía.
 * <p>
 * Incluye el precio, descripción y tipo de gasto (servicio o consumo).
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class GastoAdicionalDto {
    private final Float precio;
    private final String descripcion;
    private final TipoGastoAdicional tipoGastoAdicional;
}
