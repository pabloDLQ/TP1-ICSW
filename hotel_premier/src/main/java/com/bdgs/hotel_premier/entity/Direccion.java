package com.bdgs.hotel_premier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una dirección postal.
 * 
 * <p>Almacena la información completa de una dirección incluyendo calle,
 * número, piso, departamento, código postal, localidad, provincia y país.
 * Se utiliza para registrar las direcciones de huéspedes y personas jurídicas.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Entity
@Table(name = "direccion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calle;
    private String numero;
    private String nroDepartamento;
    private String piso;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private String pais;
}

