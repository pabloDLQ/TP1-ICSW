package com.bdgs.hotel_premier.entity;

import com.bdgs.hotel_premier.enums.TipoMoneda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un pago realizado en efectivo.
 * 
 * <p>Extiende de {@link MedioPago} y agrega el atributo de vuelto
 * para registrar el cambio devuelto al cliente.</p>
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
public class PagoEfectivo extends MedioPago {
    private Float vuelto;
    private TipoMoneda moneda;
}
