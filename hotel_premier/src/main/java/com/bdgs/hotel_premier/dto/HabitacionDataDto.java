package com.bdgs.hotel_premier.dto;

import com.bdgs.hotel_premier.entity.TipoHabitacion;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para representar datos completos de una habitación.
 * <p>
 * Incluye el ID y la entidad completa {@link TipoHabitacion} con toda su información.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Builder
@Getter
public class HabitacionDataDto {
    private final Long id;
    private final TipoHabitacion tipo;
}
