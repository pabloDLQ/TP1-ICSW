package com.bdgs.hotel_premier.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO simplificado para representar información básica de una habitación.
 * <p>
 * Contiene solo el ID y el tipo de habitación como texto.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class HabitacionDto {
    private final Long id;
    private final String tipo;
}
