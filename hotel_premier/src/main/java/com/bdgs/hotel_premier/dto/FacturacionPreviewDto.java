package com.bdgs.hotel_premier.dto;

import java.util.List;

import com.bdgs.hotel_premier.enums.TipoFactura;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO que representa la previsualización de los datos de facturación
 * antes de crear la factura definitiva (Paso 6 - Items pendientes a facturar).
 * 
 * <p>Muestra todos los items disponibles para facturar, montos calculados
 * y el tipo de factura según la posición IVA del responsable de pago.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class FacturacionPreviewDto {
    private final String responsablePagoNombre;
    private final String responsablePagoApellido;
    private final String responsablePagoCuit;
    
    private final Float valorEstadia;
    private final Float consumoTotalHabitacion;
    private final List<GastoAdicionalDto> consumosIndividuales;
    
    private final Float totalAPagar;
    private final TipoFactura tipoFactura;
}
