package com.bdgs.hotel_premier.entity;

import java.time.LocalDate;
import java.util.Set;

import com.bdgs.hotel_premier.enums.PosicionIva;
import com.bdgs.hotel_premier.enums.TipoDocumento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un huésped del hotel.
 * 
 * <p>Contiene la información personal del huésped incluyendo datos de identificación,
 * contacto, dirección y posición ante el IVA. Cada huésped se identifica de forma única
 * por la combinación de tipo y número de documento.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(
    name = "huesped",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tipoDocumento", "numeroDocumento"})
    }
)
@NoArgsConstructor           // Obligatorio para JPA
@AllArgsConstructor         // Necesario para el builder
@Builder
@Getter
@Setter
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apellido;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    private String numeroDocumento;
    private String cuit;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PosicionIva posicionIva = PosicionIva.CONSUMIDOR_FINAL;

    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;

    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;

    @ManyToMany(mappedBy = "huespedes")
    private Set<Estadia> estadias;
}
