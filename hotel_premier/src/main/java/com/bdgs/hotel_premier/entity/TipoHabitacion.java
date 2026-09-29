package com.bdgs.hotel_premier.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un tipo o categoría de habitación.
 * 
 * <p>Define las características y precio de una categoría de habitación,
 * como por ejemplo: Simple, Doble, Suite, etc. Cada tipo especifica
 * su capacidad máxima y costo por noche.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_habitacion")
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private Float costoPorNoche;
    private Integer capacidad;

}
