package com.bdgs.hotel_premier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a una persona física como responsable de pago.
 * 
 * <p>Una PersonaFisica es un {@link Huesped} que actúa como responsable
 * de pago de una o más facturas. Hereda de {@link ResponsablePago} y agrega
 * la referencia al huésped asociado.</p>
 * 
 * <p>Utiliza estrategia de herencia JOINED, por lo que los datos se almacenan
 * en dos tablas relacionadas: responsable_pago y persona_fisica.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "persona_fisica")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonaFisica extends ResponsablePago {
    
    @ManyToOne
    @JoinColumn(name = "huesped_id")
    private Huesped huesped;
}
