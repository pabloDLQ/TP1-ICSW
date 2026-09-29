package com.bdgs.hotel_premier.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para solicitudes de creación de reservas.
 * <p>
 * Contiene datos del titular y una lista de ítems de reserva, cada uno
 * representando una habitación con su rango de fechas específico.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Builder
public class ReservaSolicitudDto {

    @NotBlank
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El apellido solo puede contener letras y espacios")
    private final String apellido;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El nombre solo puede contener letras y espacios")
    private final String nombre;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{7,15}$", message = "El teléfono solo puede contener dígitos, opcionalmente empezando con +, entre 7 y 15 caracteres")
    private final String telefono;

    @NotEmpty
    @Valid
    private final List<ReservaSolicitudItemDto> items; // Cada habitación con su rango
}
