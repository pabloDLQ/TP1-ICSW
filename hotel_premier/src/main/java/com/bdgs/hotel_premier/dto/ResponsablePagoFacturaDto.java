package com.bdgs.hotel_premier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la selección de responsable de pago para preview y creación de factura.
 * 
 * <p>Puede ser:</p>
 * <ul>
 *   <li>Un huésped existente (mediante huespedId)</li>
 *   <li>Una persona jurídica existente (mediante cuit)</li>
 * </ul>
 * 
 * <p><strong>IMPORTANTE:</strong></p>
 * <ul>
 *   <li>PersonaFisica: Se crea automáticamente si el huésped no estaba registrado como responsable</li>
 *   <li>PersonaJuridica: DEBE existir en el sistema (validado previamente en el preview)</li>
 * </ul>
 * 
 * <p>Solo uno de los dos campos (huespedId o cuit) debe estar presente.</p>
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
public class ResponsablePagoFacturaDto {
    /**
     * ID del huésped si el responsable es uno de los integrantes de la estadía.
     * Se validará que sea mayor de edad.
     */
    private Long huespedId;
    
    /**
     * CUIT de persona jurídica existente en el sistema.
     * Si no existe, se debe registrar previamente.
     */
    private String cuit;
}
