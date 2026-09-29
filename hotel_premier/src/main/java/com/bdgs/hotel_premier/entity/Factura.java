package com.bdgs.hotel_premier.entity;

import java.time.LocalDate;

import com.bdgs.hotel_premier.enums.EstadoFactura;
import com.bdgs.hotel_premier.enums.TipoFactura;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una factura emitida por una estadía en el hotel.
 * 
 * <p>Una factura documenta el monto total a cobrar por los servicios prestados,
 * incluyendo el alojamiento y los consumos adicionales. Cada factura está asociada
 * a una estadía y tiene un responsable de pago (persona física o jurídica).</p>
 * 
 * <p>La factura puede ser de tipo A o B según la posición IVA del responsable,
 * y puede estar en estado PAGADA o NO_PAGADA.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "factura")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float montoFactura;
    
    @Enumerated(EnumType.STRING)
    private TipoFactura tipo;
    
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
    
    private LocalDate fechaEmision;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "responsable_pago_id")
    private ResponsablePago responsablePago;
    
    @ManyToOne
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;

    //NOTA: Atributos comentados porque sus clases no están persistidas en la BD actualmente
    //private NotaDeCredito notaDeCredito;
    //private Pago pago; 
}
