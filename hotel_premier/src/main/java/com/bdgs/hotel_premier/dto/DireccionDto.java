package com.bdgs.hotel_premier.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar una dirección postal completa.
 * <p>
 * Incluye validaciones para todos los campos de la dirección,
 * con campos opcionales para piso y departamento.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class DireccionDto {

    @NotEmpty(message = "La calle es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$", message = "La calle solo puede contener letras, números y espacios")
    private final String calle;

    @NotEmpty(message = "El número es obligatorio")
    @Pattern(regexp = "^\\d+[A-Za-z]?$", message = "El número debe ser un valor numérico, opcionalmente seguido de una letra")
    private final String numero;

    @Pattern(regexp = "^$|^[A-Za-z0-9]+$", message = "El número de departamento solo puede contener letras y números")
    private final String nroDepartamento;

    @Pattern(regexp = "^$|^[A-Za-z0-9]+$", message = "El piso solo puede contener letras y números")
    private final String piso;

    @NotEmpty(message = "El código postal es obligatorio")
    @Pattern(regexp = "^\\d{4,10}$", message = "El código postal debe contener entre 4 y 10 dígitos")
    private final String codigoPostal;

    @NotEmpty(message = "La localidad es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La localidad solo puede contener letras y espacios")
    private final String localidad;

    @NotEmpty(message = "La provincia es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La provincia solo puede contener letras y espacios")
    private final String provincia;

    @NotEmpty(message = "El país es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El país solo puede contener letras y espacios")
    private final String pais; 
}
