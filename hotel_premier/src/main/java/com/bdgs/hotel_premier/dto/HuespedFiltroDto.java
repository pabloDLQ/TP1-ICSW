package com.bdgs.hotel_premier.dto;
import com.bdgs.hotel_premier.enums.TipoDocumento;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO para filtrar y buscar huéspedes en el sistema.
 * <p>
 * Permite búsquedas por nombre, apellido, tipo de documento y número de documento.
 * Todos los campos son opcionales para permitir búsquedas flexibles.
 * </p>
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Getter
@Builder
public class HuespedFiltroDto {
    private final String apellido;
    private final String nombre;
    private final TipoDocumento tipoDocumento;
    private final String numeroDocumento;
}
