package com.bdgs.hotel_premier.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
 * Entidad que representa un período de no disponibilidad de una habitación.
 * 
 * <p>Registra los períodos en los que una habitación no está disponible
 * para ser reservada, por ejemplo por mantenimiento, reparaciones u otros motivos.
 * Cada registro incluye el motivo y las fechas de inicio y fin de la indisponibilidad.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "no_disponibilidad_habitacion")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoDisponibilidadHabitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;
}
