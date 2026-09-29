package com.bdgs.hotel_premier.dto;

import com.bdgs.hotel_premier.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de referencia para identificar un huésped en operaciones relacionadas.
 * <p>
 * Contiene únicamente tipo de documento y número para referenciar a un huésped
 * existente sin necesidad de incluir toda su información.
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
public class HuespedRefDto {
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
}