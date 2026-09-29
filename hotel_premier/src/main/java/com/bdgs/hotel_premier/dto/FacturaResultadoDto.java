package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;
import java.util.List;

import com.bdgs.hotel_premier.enums.EstadoFactura;
import com.bdgs.hotel_premier.enums.TipoFactura;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO que representa la respuesta al crear una factura exitosamente (Paso 8 - Factura generada).
 * 
 * <p>Contiene todos los datos de la factura creada para mostrar al usuario,
 * incluyendo el detalle de consumos individuales para impresión de la factura.</p>
 * 
 * <p>Este DTO se utiliza para:</p>
 * <ul>
 *   <li>Mostrar confirmación de factura creada</li>
 *   <li>Permitir imprimir, compartir o descargar la factura</li>
 *   <li>Presentar el detalle completo de items facturados</li>
 * </ul>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class FacturaResultadoDto {
    private final Long id;
    private final Float montoFactura;
    private final TipoFactura tipo;
    private final EstadoFactura estado;
    private final LocalDate fechaEmision;
    
    private final String responsablePagoNombre;
    private final String responsablePagoCuit;
    
    private final Float valorEstadia;
    private final Float consumoTotal;
    private final List<GastoAdicionalDto> consumosIndividuales;
    private final Float totalFacturado;
}
