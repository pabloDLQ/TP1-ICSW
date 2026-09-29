package com.bdgs.hotel_premier.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para filtrar reservas por datos del titular.
 * <p>
 * Permite búsqueda por apellido (obligatorio) y nombre (opcional).
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@AllArgsConstructor
@Getter
public class ReservaFiltroDto {
    @NotBlank(message = "El apellido no puede estar vacío")
    private final String apellido;
    private final String nombre;
}
