package com.bdgs.hotel_premier.entity;

import com.bdgs.hotel_premier.enums.PosicionIva;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a una persona jurídica como responsable de pago.
 * 
 * <p>Una PersonaJuridica es una empresa o entidad legal que actúa como responsable
 * de pago de una o más facturas. Hereda de {@link ResponsablePago} y agrega
 * información específica como razón social, CUIT, posición IVA y dirección fiscal.</p>
 * 
 * <p>Utiliza estrategia de herencia JOINED, por lo que los datos se almacenan
 * en dos tablas relacionadas: responsable_pago y persona_juridica.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "persona_juridica")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonaJuridica extends ResponsablePago {
    
    private String razonSocial;
    private String cuit;
    
    @Enumerated(EnumType.STRING)
    private PosicionIva posicionIva;
    
    @ManyToOne
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;
}
