package com.bdgs.hotel_premier.mapper;
import org.springframework.stereotype.Component;
import com.bdgs.hotel_premier.dto.DireccionDto;
import com.bdgs.hotel_premier.dto.HuespedDto;
import com.bdgs.hotel_premier.dto.HuespedFiltroDto;
import com.bdgs.hotel_premier.entity.Direccion;
import com.bdgs.hotel_premier.entity.Huesped;

/**
 * Mapper para convertir entre entidades {@link Huesped} y DTOs relacionados.
 * 
 * <p>Proporciona métodos para transformar objetos de dominio a DTOs de transferencia
 * y viceversa, facilitando la separación entre capas de la aplicación.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Component
public class HuespedMapper {

    // De HuespedDto a Entidad
    public Huesped toEntity(HuespedDto dto, Direccion direccion) {
        if (dto == null) return null;

        Huesped.HuespedBuilder builder = Huesped.builder()
                .apellido(dto.getApellido())
                .nombre(dto.getNombre())
                .tipoDocumento(dto.getTipoDocumento())
                .numeroDocumento(dto.getNumeroDocumento())
                .cuit(dto.getCuit())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(direccion)
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .ocupacion(dto.getOcupacion())
                .nacionalidad(dto.getNacionalidad());

        if (dto.getPosicionIva() != null) {
            builder.posicionIva(dto.getPosicionIva());
        }

        return builder.build();
    }

    // De HuespedFiltroDto a Entidad
    public Huesped toEntity(HuespedFiltroDto dto) {
        if (dto == null) return null;
        return Huesped.builder()
                .apellido(dto.getApellido())
                .nombre(dto.getNombre())
                .tipoDocumento(dto.getTipoDocumento())
                .numeroDocumento(dto.getNumeroDocumento())
                .build();
    }

    // De Entidad a HuespedDto
    public HuespedDto toDto(Huesped entity, DireccionDto direccionDto) {
        if (entity == null) return null;
        return HuespedDto.builder()
                .apellido(entity.getApellido())
                .nombre(entity.getNombre())
                .tipoDocumento(entity.getTipoDocumento())
                .numeroDocumento(entity.getNumeroDocumento())
                .cuit(entity.getCuit())
                .posicionIva(entity.getPosicionIva())
                .fechaNacimiento(entity.getFechaNacimiento())
                .direccion(direccionDto)
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .ocupacion(entity.getOcupacion())
                .nacionalidad(entity.getNacionalidad())
                .build();
    }

    // Actualiza entidad existente con datos del DTO (sin tocar la dirección)
    public void updateEntity(Huesped target, HuespedDto source) {
        if (target == null || source == null) return;
        target.setApellido(source.getApellido());
        target.setNombre(source.getNombre());
        target.setTipoDocumento(source.getTipoDocumento());
        target.setNumeroDocumento(source.getNumeroDocumento());
        target.setCuit(source.getCuit());
        target.setFechaNacimiento(source.getFechaNacimiento());
        target.setTelefono(source.getTelefono());
        target.setEmail(source.getEmail());
        target.setOcupacion(source.getOcupacion());
        target.setNacionalidad(source.getNacionalidad());
        target.setPosicionIva(source.getPosicionIva());
        // La dirección se maneja desde el service para asegurar entidad persistida
    }

    // De Entidad a HuespedFiltroDto
    public HuespedFiltroDto toFiltroDto(Huesped entity) {
        if (entity == null) return null;
        return HuespedFiltroDto.builder()
                .apellido(entity.getApellido())
                .nombre(entity.getNombre())
                .tipoDocumento(entity.getTipoDocumento())
                .numeroDocumento(entity.getNumeroDocumento())
                .build();
    }
}
