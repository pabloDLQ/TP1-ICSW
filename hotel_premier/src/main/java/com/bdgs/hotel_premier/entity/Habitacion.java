package com.bdgs.hotel_premier.entity;
import java.util.ArrayList;
import java.util.List;
import com.bdgs.hotel_premier.enums.TipoCama;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una habitación del hotel.
 * 
 * <p>Una habitación tiene un código único, un tipo que define sus características
 * y capacidad, y una lista de camas disponibles. Se utiliza para gestionar
 * la disponibilidad y asignación durante las estadías.</p>
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
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "habitacion_camas",
            joinColumns = @JoinColumn(name = "habitacion_id")
    )
    @Column(name = "tipo_cama")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<TipoCama> camas = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "tipo_habitacion_id")
    private TipoHabitacion tipoHabitacion;
}
