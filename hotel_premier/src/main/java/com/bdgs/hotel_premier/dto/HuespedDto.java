package com.bdgs.hotel_premier.dto;

import java.time.LocalDate;

import com.bdgs.hotel_premier.enums.PosicionIva;
import com.bdgs.hotel_premier.enums.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar la información completa de un huésped en el sistema.
 * <p>
 * Incluye validaciones completas para todos los campos obligatorios y opcionales,
 * asegurando la integridad de los datos del huésped.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HuespedDto {
    
    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El apellido solo puede contener letras y espacios")
    private final String apellido;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El nombre solo puede contener letras y espacios")
    private final String nombre;

    @NotNull(message = "El tipo de documento es obligatorio")
    private final TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}$", message = "El número de documento debe tener entre 6 y 12 caracteres alfanuméricos")
    private final String numeroDocumento;

    @Pattern(regexp = "^$|^\\d{2}-?\\d{8}-?\\d$", message = "El CUIT debe tener el formato XX-XXXXXXXX-X")
    private final String cuit;

    private final PosicionIva posicionIva;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private final LocalDate fechaNacimiento;

    @NotNull(message = "La dirección es obligatoria")
    @Valid
    private final DireccionDto direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?\\d{7,15}$",
            message = "El teléfono solo puede contener dígitos, opcionalmente empezando con +, entre 7 y 15 caracteres")
    private final String telefono;

    @Email(message = "Formato de email inválido")
    private final String email;

    @NotBlank(message = "La ocupación es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La ocupación solo puede contener letras y espacios")
    private final String ocupacion;

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "La nacionalidad solo puede contener letras y espacios")
    private final String nacionalidad;
}
