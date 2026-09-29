package com.bdgs.hotel_premier.entity;

import com.bdgs.hotel_premier.enums.MarcaTarjeta;
import com.bdgs.hotel_premier.enums.TipoTarjeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa un pago realizado con tarjeta de crédito o débito.
 * 
 * <p>Extiende de {@link MedioPago} y agrega información específica de la tarjeta
 * como el número, fecha de caducidad, CVV, tipo y marca.</p>
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
public class PagoTarjeta extends MedioPago {
    private Integer nroTarjeta;
    private LocalDate fechaCaducidad;
    private Integer cvv;
    private TipoTarjeta tipo;
    private MarcaTarjeta marca;
}
