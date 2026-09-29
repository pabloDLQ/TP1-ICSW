package com.bdgs.hotel_premier.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para solicitudes de creación de estadías (check-in).
 * <p>
 * Incluye el huésped principal, acompañantes opcionales y los ítems
 * que definen las habitaciones y fechas de la estadía.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadiaSolicitudDto {
    private HuespedRefDto huespedPrincipal;
    private List<HuespedRefDto> acompanantes;
    private List<EstadiaSolicitudItemDto> items;
}