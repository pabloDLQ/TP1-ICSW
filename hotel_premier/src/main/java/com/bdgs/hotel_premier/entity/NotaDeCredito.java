package com.bdgs.hotel_premier.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una nota de crédito.
 * 
 * <p>Una nota de crédito documenta el monto de devolución que recibirá
 * un huésped por cancelaciones, reembolsos u otros conceptos.</p>
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
public class NotaDeCredito {
    private Float monto;
    private LocalDate fechaEmision;
    private List<Factura> facturasAsociadas;
}
