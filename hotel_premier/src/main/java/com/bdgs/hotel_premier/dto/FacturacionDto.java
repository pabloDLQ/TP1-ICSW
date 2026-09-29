package com.bdgs.hotel_premier.dto;
import java.util.List;
import java.util.Set;

import com.bdgs.hotel_premier.enums.TipoFactura;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar información resumida de facturación de una estadía.
 * <p>
 * Incluye los huéspedes, gastos adicionales, valor de estadía y totales,
 * junto con el tipo de factura a generar.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class FacturacionDto {
    private final Set<HuespedDataDto> huespedes;
    private final List<GastoAdicionalDto> gastosAdicionales;
    private final Double valorEstadia;
    private final Double totalFacturado;
    private final Double totalGastosAdicionales;
    private final TipoFactura tipoFactura;
}
