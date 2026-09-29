package com.bdgs.hotel_premier.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad abstracta que representa a un responsable de pago de facturas.
 * 
 * <p>Esta clase es la base de la jerarquía de responsables de pago, que puede ser:</p>
 * <ul>
 *   <li>{@link PersonaFisica}: Un huésped que actúa como responsable</li>
 *   <li>{@link PersonaJuridica}: Una empresa o entidad legal</li>
 * </ul>
 * 
 * <p>Utiliza estrategia de herencia JOINED, donde cada subclase tiene su propia
 * tabla con una clave foránea a la tabla responsable_pago.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "responsable_pago")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class ResponsablePago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "responsablePago")
    private List<Factura> facturas;
}
