package com.bdgs.hotel_premier.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa un pago realizado por un huésped.
 * 
 * <p>Contiene el monto, fecha, moneda y el medio de pago utilizado.
 * El medio de pago puede ser efectivo, tarjeta o cheque.</p>
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
public class Pago {
    private Float montoPago;
    private LocalDate fechaPago;
    private List<MedioPago> medioPago;
}
