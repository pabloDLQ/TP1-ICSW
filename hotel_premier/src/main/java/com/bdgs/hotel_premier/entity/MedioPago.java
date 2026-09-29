package com.bdgs.hotel_premier.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Clase abstracta que representa un medio de pago.
 * 
 * <p>Contiene los datos comunes a todos los medios de pago como la fecha
 * y el monto. Las clases concretas extienden esta clase para implementar
 * tipos específicos de pago (efectivo, tarjeta, cheque).</p>
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
public abstract class MedioPago {
    private LocalDate fechaMedioPago;
    private Float montoMedioPago;
}
