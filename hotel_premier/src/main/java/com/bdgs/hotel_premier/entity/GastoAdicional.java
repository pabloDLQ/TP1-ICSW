package com.bdgs.hotel_premier.entity;

import com.bdgs.hotel_premier.enums.TipoGastoAdicional;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un gasto adicional asociado a una estadía.
 * 
 * <p>Los gastos adicionales son consumos o servicios extras que el huésped
 * utiliza durante su estadía, como room service, lavandería, minibar, etc.
 * Cada gasto se registra con su precio, descripción y tipo.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "gasto_adicional")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GastoAdicional {

    @Id
    private Long id;

    private Float precio;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoGastoAdicional tipoGastoAdicional;

    @ManyToOne
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;
}
