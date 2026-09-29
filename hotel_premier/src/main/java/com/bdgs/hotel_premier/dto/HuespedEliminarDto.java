package com.bdgs.hotel_premier.dto;

import com.bdgs.hotel_premier.enums.TipoDocumento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para la eliminación de huéspedes del sistema.
 * <p>
 * Requiere solo el tipo de documento y número de documento para identificar
 * de manera única al huésped a eliminar.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class HuespedEliminarDto {
    @NotNull(message = "El tipo de documento es obligatorio")
    private final TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}$", message = "El número de documento debe tener entre 6 y 12 caracteres alfanuméricos")
    private final String numeroDocumento;
}
