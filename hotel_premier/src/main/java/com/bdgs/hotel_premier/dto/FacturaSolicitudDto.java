package com.bdgs.hotel_premier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para crear una factura (Paso 7 - Crear factura).
 * 
 * <p>Incluye los items seleccionados por el usuario (estadía y/o consumos)
 * y el responsable de pago elegido (huésped o persona jurídica).</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaSolicitudDto {
    @NotNull(message = "El ID de la estadía es obligatorio")
    private Long estadiaId;
    
    @NotNull(message = "Debe especificar un responsable de pago")
    private ResponsablePagoFacturaDto responsablePago;
    
    /**
     * Indica si se debe facturar el valor de la estadía (alojamiento)
     */
    @NotNull(message = "Debe indicar si factura la estadía")
    private Boolean facturarEstadia;
    
    /**
     * Indica si se deben facturar los consumos adicionales de la habitación
     */
    @NotNull(message = "Debe indicar si factura los consumos")
    private Boolean facturarConsumos;
}
