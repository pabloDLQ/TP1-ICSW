package com.bdgs.hotel_premier.entity;

import java.time.LocalDate;

import com.bdgs.hotel_premier.enums.EstadoReserva;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una reserva de habitación en el hotel.
 * 
 * <p>Una reserva registra la intención de un huésped de ocupar una habitación
 * en un período determinado. Puede estar en diferentes estados (PENDIENTE, CONFIRMADA,
 * CANCELADA, etc.) y se asocia posteriormente a una estadía cuando se efectiviza.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "reserva")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    @OneToOne(mappedBy="reserva")
    private Estadia estadia;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;
}
