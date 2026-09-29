package com.bdgs.hotel_premier.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa una estadía de huéspedes en el hotel.
 * 
 * <p>Una estadía registra la ocupación de una habitación por uno o más huéspedes
 * durante un período determinado. Incluye la información de fechas, habitación asignada,
 * huéspedes alojados y gastos adicionales generados durante la estadía.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "estadia")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    @ManyToMany
    @JoinTable(
        name = "estadia_huesped",
        joinColumns = @JoinColumn(name = "estadia_id"),
        inverseJoinColumns = @JoinColumn(name = "huesped_id")
    )
    private Set<Huesped> huespedes;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @OneToMany(mappedBy = "estadia")
    private List<GastoAdicional> gastosAdicionales;
    
    @OneToMany(mappedBy = "estadia")
    private List<Factura> facturas;

}
