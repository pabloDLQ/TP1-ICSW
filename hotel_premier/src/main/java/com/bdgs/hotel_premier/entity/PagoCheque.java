package com.bdgs.hotel_premier.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa un pago realizado con cheque.
 * 
 * <p>Extiende de {@link MedioPago} y agrega información específica del cheque
 * como el número de orden y la fecha de emisión.</p>
 * 
 * <p><strong>Nota:</strong> Esta clase forma parte del modelo de dominio
 * completo pero no está persistida en la base de datos actual.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagoCheque extends MedioPago {
    private Integer numeroOrden;
    private LocalDate fechaEmision;
}
